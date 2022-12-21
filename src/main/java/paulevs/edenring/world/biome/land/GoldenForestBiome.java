package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class GoldenForestBiome extends EdenRingBiome.Config {
    public GoldenForestBiome() {
        super(EdenBiomes.GOLDEN_FOREST.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .plantsColor(255, 174, 100)
                .skyColor(113, 178, 255)
                .fogColor(183, 212, 255)
                .loop(EdenSounds.AMBIENCE_GOLDEN_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
                .feature(EdenFeatures.AURITIS_TREE)
                .feature(EdenFeatures.EDEN_MOSS_LAYER)
                .feature(EdenFeatures.GOLDEN_GRASS)
                .feature(EdenFeatures.EDEN_VINE)
                .feature(EdenFeatures.ORE_GOLD)
                .feature(EdenFeatures.PARIGNUM);
    }
}
