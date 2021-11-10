package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class StoneGardenBiome extends BCLBiome {
	public StoneGardenBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(
			EdenBiomes.addDefaultFeatures(new BCLBiomeDef(EdenRing.makeID("stone_garden"))
				.loadConfigValues(config)
				.setCategory(BiomeCategory.PLAINS)
				.setSurface(surfaceBuilder)
				.setPlantsColor(162, 190, 113)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.MOSS_PATCH)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.FOREST_ROCK)
				.addFeature(EdenFeatures.COBBLE_FLOOR)
				.addFeature(EdenFeatures.MOSS_FLOOR)
				.addFeature(EdenFeatures.MOSS_LAYER)
				.addFeature(EdenFeatures.EDEN_MOSS_LAYER)
				.addFeature(EdenFeatures.STONE_PILLAR)
				.addFeature(EdenFeatures.VIOLUM_RARE)
				.addFeature(EdenFeatures.EDEN_VINE)
				.addFeature(EdenFeatures.ROOTS)
				.setSkyColor(113, 178, 255)
				.setFogColor(183, 212, 255)
		));
	}
}
