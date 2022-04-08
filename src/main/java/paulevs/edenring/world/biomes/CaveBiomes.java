package paulevs.edenring.world.biomes;

import net.minecraft.world.level.block.Blocks;
import paulevs.edenring.EdenRing;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.world.biomes.BCLBiome;

public class CaveBiomes {
	public static BCLBiome makeErodedCaveBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("eroded_cave"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		return builder
			.plantsColor(162, 190, 113)
			.surface(SurfaceRuleBuilder
				.start()
				.surface(Blocks.DRIPSTONE_BLOCK.defaultBlockState())
				.subsurface(Blocks.DRIPSTONE_BLOCK.defaultBlockState(), 3)
				.build()
			).build();
	}
}
