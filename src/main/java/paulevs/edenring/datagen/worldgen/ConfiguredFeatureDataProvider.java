package paulevs.edenring.datagen.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

public class ConfiguredFeatureDataProvider {
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        BCLFeatureBuilder.registerUnbound(ctx);
    }
}
