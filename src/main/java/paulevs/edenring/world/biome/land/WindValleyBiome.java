package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenParticles;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class WindValleyBiome extends EdenRingBiome.Config {
    public WindValleyBiome() {
        super(EdenBiomes.WIND_VALLEY.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .skyColor(113, 178, 255)
                .fogColor(183, 212, 255)
                .grassColor(225, 84, 72)
                .foliageColor(230, 63, 50)
                .loop(EdenSounds.AMBIENCE_WIND_VALLEY)
                .particles(EdenParticles.WIND_PARTICLE, 0.001F)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(EdenFeatures.VIOLUM_RARE)
                .feature(EdenFeatures.LONLIX)
                .feature(EdenFeatures.PARIGNUM);
    }
}
