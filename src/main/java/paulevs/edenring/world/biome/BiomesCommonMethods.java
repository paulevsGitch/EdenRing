package paulevs.edenring.world.biome;

import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;

public class BiomesCommonMethods {
	public static void addDefaultLandFeatures(BCLBiomeBuilder builder) {
		builder
			//.carver(Carving.AIR, EdenCarvers.CAVE_CONFIGURED)
			.feature(EdenFeatures.SLATE_LAYER)
			.feature(EdenFeatures.CALCITE_LAYER)
			.feature(EdenFeatures.TUFF_LAYER)
			.feature(Decoration.UNDERGROUND_DECORATION, CavePlacements.AMETHYST_GEODE)
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
}
