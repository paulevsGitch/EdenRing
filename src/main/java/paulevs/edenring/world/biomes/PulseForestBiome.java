package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class PulseForestBiome extends BCLBiome {
	public PulseForestBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(EdenBiomes.addDefaultFeatures(
			new BCLBiomeDef(EdenRing.makeID("pulse_forest"))
				.loadConfigValues(config)
				.setSurface(surfaceBuilder)
				.setPlantsColor(121, 238, 248)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_LARGE_FERN)
				.addFeature(EdenFeatures.PULSE_TREE)
				.addFeature(EdenFeatures.VIOLUM_DENSE)
				.addFeature(EdenFeatures.EDEN_VINE)
				.setSkyColor(113, 178, 255)
				.setFogColor(115, 235, 242)
		));
	}
}