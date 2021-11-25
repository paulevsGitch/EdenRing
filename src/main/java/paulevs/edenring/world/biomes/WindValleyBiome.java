package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenParticles;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class WindValleyBiome extends BCLBiome {
	public WindValleyBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(
			EdenBiomes.addDefaultFeatures(new BCLBiomeDef(EdenRing.makeID("wind_valley"))
				.loadConfigValues(config)
				.setSurface(surfaceBuilder)
				.setGrassColor(225, 84, 72)
				.setFoliageColor(230, 63, 50)
				.setSkyColor(113, 178, 255)
				.setFogColor(183, 212, 255)
				.setParticles(EdenParticles.WIND_PARTICLE, 0.001F)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST)
				.addFeature(EdenFeatures.VIOLUM_RARE)
				.addFeature(EdenFeatures.LONLIX)
			));
	}
}
