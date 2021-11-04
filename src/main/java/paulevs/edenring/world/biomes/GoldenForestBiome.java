package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.world.features.EdenFeatures;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class GoldenForestBiome extends BCLBiome {
	public GoldenForestBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(EdenBiomes.addDefaultFeatures(
			new BCLBiomeDef(EdenRing.makeID("golden_forest"))
				.loadConfigValues(config)
				.setCategory(BiomeCategory.FOREST)
				.setSurface(surfaceBuilder)
				.setPlantsColor(255, 174, 100)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_LARGE_FERN)
				.addFeature(EdenFeatures.AURITIS_TREE)
				.addFeature(EdenFeatures.EDEN_MOSS_LAYER)
				.addFeature(EdenFeatures.GOLDEN_GRASS)
				.addFeature(EdenFeatures.EDEN_VINE)
				.setSkyColor(113, 178, 255)
				.setFogColor(183, 212, 255)
		));
	}
}
