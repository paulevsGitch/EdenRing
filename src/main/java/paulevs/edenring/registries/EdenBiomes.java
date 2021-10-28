package paulevs.edenring.registries;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.biomes.StoneGardenBiome;
import ru.bclib.api.BiomeAPI;
import ru.bclib.config.EntryConfig;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;

public class EdenBiomes {
	private static final IdConfig CONFIG = new EntryConfig(EdenRing.MOD_ID, "biomes");
	
	private static final SurfaceBuilderBaseConfiguration CONFIG_GRASS = new SurfaceBuilderBaseConfiguration(
		EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState(),
		Blocks.DIRT.defaultBlockState(),
		Blocks.DIRT.defaultBlockState()
	);
	private static final ConfiguredSurfaceBuilder DEFAULT_BUILDER = SurfaceBuilder.DEFAULT.configured(CONFIG_GRASS);
	
	public static final BCLBiome STONE_GARDEN = register(new StoneGardenBiome(CONFIG, DEFAULT_BUILDER));
	
	public static void init() {
		CONFIG.saveChanges();
	}
	
	private static BCLBiome register(BCLBiome biome) {
		return BiomeAPI.registerBiome(biome);
	}
	
	public static int correctColor(int color) {
		color *= 1.33F;
		return color > 255 ? 255 : color;
	}
}
