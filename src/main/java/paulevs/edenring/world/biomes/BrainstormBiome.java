package paulevs.edenring.world.biomes;

import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.world.features.EdenFeatures;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class BrainstormBiome extends BCLBiome {
	public BrainstormBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(EdenBiomes.addDefaultFeatures(
			new BCLBiomeDef(EdenRing.makeID("brainstorm"))
				.loadConfigValues(config)
				.setCategory(BiomeCategory.FOREST)
				.setSurface(surfaceBuilder)
				.setPlantsColor(121, 238, 248)
				.addFeature(EdenFeatures.BRAIN_TREE)
				.setSkyColor(113, 178, 255)
				.setFogColor(113, 178, 255)
		));
	}
}