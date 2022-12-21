package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class StoneGardenBiome extends EdenRingBiome.Config {
    public StoneGardenBiome() {
        super(EdenBiomes.STONE_GARDEN.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .plantsColor(162, 190, 113)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.FOREST_ROCK)
                .feature(EdenFeatures.COBBLE_FLOOR)
                .feature(EdenFeatures.MOSS_FLOOR)
                .feature(EdenFeatures.MOSS_LAYER)
                .feature(EdenFeatures.EDEN_MOSS_LAYER)
                .feature(EdenFeatures.STONE_PILLAR)
                .feature(EdenFeatures.LIMPHIUM)
                .feature(EdenFeatures.VIOLUM_RARE)
                .feature(EdenFeatures.EDEN_VINE)
                .feature(EdenFeatures.ROOTS)
                .feature(EdenFeatures.PARIGNUM);
    }
}
