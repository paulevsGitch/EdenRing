package paulevs.edenring.world.biome.air;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenParticles;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class SkyColonyBiome extends EdenRingBiome.Config {
    public SkyColonyBiome() {
        super(EdenBiomes.SKY_COLONY.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultVoidFeatures(builder);
        builder
                .spawn(EdenEntities.DISKWING, 20, 3, 6)
                .fogColor(0x84d341)
                .waterColor(0x1e7d56)
                .plantsColor(0x1e7d56)
                .particles(EdenParticles.YOUNG_VOLVOX, 0.0001F)
                .feature(EdenFeatures.VOLVOX)
                .feature(EdenFeatures.PARIGNUM);
    }
}
