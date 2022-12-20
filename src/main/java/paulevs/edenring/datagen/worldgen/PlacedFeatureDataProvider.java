package paulevs.edenring.datagen.worldgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLPlacedFeatureBuilder;

public class PlacedFeatureDataProvider {
    public static void bootstrap(BootstapContext<PlacedFeature> ctx) {
        BCLPlacedFeatureBuilder.registerUnbound(ctx);
    }
}
