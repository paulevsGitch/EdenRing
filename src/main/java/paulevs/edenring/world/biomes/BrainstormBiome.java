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

public class BrainstormBiome extends BCLBiome {
	public BrainstormBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(EdenBiomes.addDefaultFeatures(
			new BCLBiomeDef(EdenRing.makeID("brainstorm"))
				.loadConfigValues(config)
				.setSurface(surfaceBuilder)
				.setPlantsColor(200, 200, 200)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST)
				.addFeature(Decoration.VEGETAL_DECORATION, Features.PATCH_TALL_GRASS)
				.addFeature(EdenFeatures.IRON_GRASS)
				.addFeature(EdenFeatures.COPPER_GRASS)
				.addFeature(EdenFeatures.GOLD_GRASS)
				.addFeature(EdenFeatures.BRAIN_TREE)
				.addFeature(EdenFeatures.LAYERED_IRON)
				.addFeature(EdenFeatures.LAYERED_COPPER)
				.addFeature(EdenFeatures.LAYERED_GOLD)
				.setSkyColor(113, 178, 255)
				.setFogColor(180, 180, 180)
				.setFogDensity(2.5F)
		));
	}
}