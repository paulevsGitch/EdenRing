package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class LakesideDesertBiome extends EdenRingBiome.Config {
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    private static final BlockState SAND = Blocks.SAND.defaultBlockState();

    public LakesideDesertBiome() {
        super(EdenBiomes.LAKESIDE_DESERT.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .fogDensity(1.75F)
                .skyColor(113, 178, 255)
                .fogColor(237, 235, 203)
                .grassColor(246, 222, 173)
                .foliageColor(247, 165, 115)
                .loop(EdenSounds.AMBIENCE_LAKESIDE_DESSERT)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
                .feature(EdenFeatures.GRAVEL_FLOOR)
                .feature(EdenFeatures.GRASS_FLOOR)
                .feature(EdenFeatures.AQUATUS);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EdenRingBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return SANDSTONE;
            }

            @Override
            public BlockState getUnderMaterial() {
                return SANDSTONE;
            }

            @Override
            public boolean generateSubSurfaceRule() {
                return false;
            }

            @Override
            public SurfaceRuleBuilder surface() {
                return super.surface().subsurface(SAND, 3).ceil(SANDSTONE).filler(SANDSTONE);
            }
        };
    }
}
