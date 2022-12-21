package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class PulseForestBiome extends EdenRingBiome.Config {
    public PulseForestBiome() {
        super(EdenBiomes.PULSE_FOREST.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .skyColor(113, 178, 255)
                .fogColor(115, 235, 242)
                .plantsColor(121, 238, 248)
                .loop(EdenSounds.AMBIENCE_PULSE_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
                .feature(EdenFeatures.PULSE_TREE)
                .feature(EdenFeatures.LIMPHIUM)
                .feature(EdenFeatures.VIOLUM_DENSE)
                .feature(EdenFeatures.EDEN_VINE);
    }
}
