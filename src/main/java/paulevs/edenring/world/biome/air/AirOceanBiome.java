package paulevs.edenring.world.biome.air;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class AirOceanBiome extends EdenRingBiome.Config {
    public AirOceanBiome() {
        super(EdenBiomes.AIR_OCEAN.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultVoidFeatures(builder);
        builder.spawn(EdenEntities.DISKWING, 20, 3, 6);
    }
}
