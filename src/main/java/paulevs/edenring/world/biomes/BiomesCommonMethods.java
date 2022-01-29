package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenCarvers;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.surface.SurfaceRuleBuilder;

public class BiomesCommonMethods {
	public static void addDefaultLandFeatures(BCLBiomeBuilder builder) {
		builder
			.carver(Carving.AIR, EdenCarvers.CAVE_CONFIGURED)
			.feature(EdenFeatures.SLATE_LAYER)
			.feature(EdenFeatures.CALCITE_LAYER)
			.feature(EdenFeatures.TUFF_LAYER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRANITE_UPPER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANDESITE_UPPER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_DIORITE_UPPER)
			.feature(EdenFeatures.SMALL_ISLAND)
			.feature(EdenFeatures.ORE_MOSSY_COBBLE)
			.feature(EdenFeatures.ORE_COBBLE)
			.feature(EdenFeatures.ORE_COAL)
			.feature(EdenFeatures.ORE_IRON)
			.feature(EdenFeatures.ORE_COPPER)
			.feature(EdenFeatures.GRAVILITE_CRYSTAL);
	}
	
	public static void addDefaultVoidFeatures(BCLBiomeBuilder builder) {
		builder
			.feature(EdenFeatures.SMALL_ISLAND)
			.feature(EdenFeatures.GRAVILITE_CRYSTAL);
	}
	
	public static void addDefaultSurface(BCLBiomeBuilder builder) {
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
	}
	
	public static void addDefaultSounds(BCLBiomeBuilder builder) {
		builder.music(EdenSounds.MUSIC_COMMON);
	}
	
	public static void setDefaultColors(BCLBiomeBuilder builder) {
		builder.skyColor(113, 178, 255).fogColor(183, 212, 255).waterFogColor(329011).waterColor(4159204);
	}
}
