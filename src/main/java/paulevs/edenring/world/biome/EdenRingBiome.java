package paulevs.edenring.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeSettings;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import org.jetbrains.annotations.NotNull;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenSounds;

import java.util.List;
import java.util.Optional;

public class EdenRingBiome extends BCLBiome implements SurfaceMaterialProvider {
    public static final Codec<EdenRingBiome> CODEC = RecordCodecBuilder.create(instance ->
            codecWithSettings(
                    instance,
                    SurfaceMaterialProvider.CODEC.fieldOf("surface")
                            .orElse(Config.DEFAULT_MATERIAL)
                            .forGetter(o -> o.surfMatProv)
            ).apply(instance, EdenRingBiome::new)
    );
    public static final KeyDispatchDataCodec<EdenRingBiome> KEY_CODEC = KeyDispatchDataCodec.of(CODEC);

    @Override
    public KeyDispatchDataCodec<? extends BCLBiome> codec() {
        return KEY_CODEC;
    }

    protected EdenRingBiome(
            float terrainHeight,
            float fogDensity,
            float genChance,
            int edgeSize,
            boolean vertical,
            Optional<ResourceLocation> edge,
            ResourceLocation biomeID,
            Optional<List<Climate.ParameterPoint>> parameterPoints,
            Optional<ResourceLocation> biomeParent,
            Optional<String> intendedType,
            SurfaceMaterialProvider surface
    ) {
        super(
                terrainHeight,
                fogDensity,
                genChance,
                edgeSize,
                vertical,
                edge,
                biomeID,
                parameterPoints,
                biomeParent,
                intendedType
        );
        this.surfMatProv = surface;
    }

    public static class DefaultSurfaceMaterialProvider implements SurfaceMaterialProvider {
        public static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
        public static final BlockState DRIPSTONE_BLOCK = Blocks.DRIPSTONE_BLOCK.defaultBlockState();
        public static final BlockState EDEN_GRASS_BLOCK = EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState();
        public static final BlockState EDEN_MOSS = EdenBlocks.EDEN_MOSS.defaultBlockState();
        public static final BlockState EDEN_MYCELIUM = EdenBlocks.EDEN_MYCELIUM.defaultBlockState();
        public static final BlockState STONE = Blocks.STONE.defaultBlockState();

        @Override
        public BlockState getTopMaterial() {
            return EDEN_GRASS_BLOCK;
        }

        @Override
        public BlockState getAltTopMaterial() {
            return getTopMaterial();
        }

        @Override
        public BlockState getUnderMaterial() {
            return DIRT;
        }

        public int subSurfaceDepth() {
            return 3;
        }

        @Override
        public boolean generateFloorRule() {
            return true;
        }

        public boolean generateSubSurfaceRule() {
            return true;
        }

        @Override
        public SurfaceRuleBuilder surface() {
            SurfaceRuleBuilder builder = SurfaceRuleBuilder.start();

            if (generateFloorRule() && getTopMaterial() != getUnderMaterial()) {
                if (getTopMaterial() == getAltTopMaterial()) {
                    builder.floor(getTopMaterial());
                } else {
                    builder.chancedFloor(getTopMaterial(), getAltTopMaterial());
                }
            }
            if (generateSubSurfaceRule()) {
                builder.subsurface(getUnderMaterial(), subSurfaceDepth());
            }
            return builder;
        }
    }

    public abstract static class Config {
        public static final SurfaceMaterialProvider DEFAULT_MATERIAL = new DefaultSurfaceMaterialProvider();

        public final ResourceLocation ID;

        protected Config(String name) {
            this.ID = EdenRing.makeID(name);
        }

        protected Config(ResourceLocation ID) {
            this.ID = ID;
        }

        protected abstract void addCustomBuildData(BCLBiomeBuilder builder);

        public BCLBiomeBuilder.BiomeSupplier<EdenRingBiome> getSupplier() {
            return EdenRingBiome::new;
        }

        protected SurfaceMaterialProvider surfaceMaterial() {
            return DEFAULT_MATERIAL;
        }
    }

    public EdenRingBiome(ResourceKey<Biome> biomeID, BCLBiomeSettings settings) {
        super(biomeID, settings);
    }

    public static EdenRingBiome create(Config biomeConfig, BiomeAPI.BiomeType type) {
        return create(biomeConfig, type, null);
    }

    public static EdenRingBiome createSubBiome(Config data, @NotNull BCLBiome parentBiome) {
        return create(data, parentBiome.getIntendedType(), parentBiome);
    }

    private static EdenRingBiome create(Config biomeConfig, BiomeAPI.BiomeType type, BCLBiome parentBiome) {
        BCLBiomeBuilder builder = BCLBiomeBuilder
                .start(biomeConfig.ID)
                .music(EdenSounds.MUSIC_COMMON)
                .waterColor(4159204)
                .waterFogColor(329011)
                .fogColor(183, 212, 255)
                .skyColor(113, 178, 255)
                .parentBiome(parentBiome)
                .surface(biomeConfig.surfaceMaterial().surface().build())
                .type(type);

        biomeConfig.addCustomBuildData(builder);

        EdenRingBiome biome = builder.build(biomeConfig.getSupplier()).biome();
        biome.setSurfaceMaterial(biomeConfig.surfaceMaterial());

        return biome;
    }

    protected SurfaceMaterialProvider surfMatProv = Config.DEFAULT_MATERIAL;

    protected void setSurfaceMaterial(SurfaceMaterialProvider prov) {
        this.surfMatProv = prov;
    }

    @Override
    public BlockState getTopMaterial() {
        return this.surfMatProv.getTopMaterial();
    }

    @Override
    public BlockState getUnderMaterial() {
        return this.surfMatProv.getUnderMaterial();
    }

    @Override
    public BlockState getAltTopMaterial() {
        return this.surfMatProv.getAltTopMaterial();
    }

    @Override
    public boolean generateFloorRule() {
        return this.surfMatProv.generateFloorRule();
    }

    @Override
    public SurfaceRuleBuilder surface() {
        return this.surfMatProv.surface();
    }

    public static BlockState findTopMaterial(BCLBiome biome) {
        return BiomeAPI.findTopMaterial(biome).orElse(Config.DEFAULT_MATERIAL.getTopMaterial());
    }

    public static BlockState findTopMaterial(Biome biome) {
        return findTopMaterial(BiomeAPI.getBiome(biome));
    }

    public static BlockState findTopMaterial(WorldGenLevel world, BlockPos pos) {
        return findTopMaterial(BiomeAPI.getBiome(world.getBiome(pos)));
    }

    public static BlockState findUnderMaterial(BCLBiome biome) {
        return BiomeAPI.findUnderMaterial(biome).orElse(Config.DEFAULT_MATERIAL.getUnderMaterial());
    }

    public static BlockState findUnderMaterial(WorldGenLevel world, BlockPos pos) {
        return findUnderMaterial(BiomeAPI.getBiome(world.getBiome(pos)));
    }

    public static List<BCLBiome> getAllBeBiomes() {
        return BiomeAPI.getAllBiomes(EdenBiomes.EDEN);
    }
}
