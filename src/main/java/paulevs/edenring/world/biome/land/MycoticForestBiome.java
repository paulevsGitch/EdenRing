package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
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

public class MycoticForestBiome extends EdenRingBiome.Config {
    public MycoticForestBiome() {
        super(EdenBiomes.MYCOTIC_FOREST.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .grassColor(220, 130, 189)
                .foliageColor(152, 90, 131)
                .skyColor(113, 178, 255)
                .fogColor(178, 112, 143)
                .loop(EdenSounds.AMBIENCE_MYCOTIC_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(EdenFeatures.BALLOON_MUSHROOM_TREE)
                .feature(EdenFeatures.TALL_BALLOON_MUSHROOM)
                .feature(EdenFeatures.BALLOON_MUSHROOM_SMALL)
                .feature(EdenFeatures.GRASS_FLOOR)
                .feature(EdenFeatures.LIMPHIUM)
                .feature(EdenFeatures.TALL_MYCOTIC_GRASS)
                .feature(EdenFeatures.MYCOTIC_GRASS)
                .feature(EdenFeatures.EDEN_VINE);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EdenRingBiome.DefaultSurfaceMaterialProvider() {
            @Override
            public BlockState getTopMaterial() {
                return EDEN_MYCELIUM;
            }
        };
    }
}
