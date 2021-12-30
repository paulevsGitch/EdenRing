package paulevs.edenring.registries;

import com.google.common.collect.Lists;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.edenring.EdenRing;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.config.EntryConfig;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;

import java.util.List;

public class EdenBiomes {
	private static final IdConfig CONFIG = new EntryConfig(EdenRing.MOD_ID, "biomes");
	public static final List<BCLBiome> BIOMES_LAND = Lists.newArrayList();
	public static final List<BCLBiome> BIOMES_CAVE = Lists.newArrayList();
	
	/*private static final SurfaceBuilderBaseConfiguration CONFIG_GRASS = new SurfaceBuilderBaseConfiguration(
		EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState(),
		Blocks.DIRT.defaultBlockState(),
		Blocks.DIRT.defaultBlockState()
	);
	private static final SurfaceBuilderBaseConfiguration CONFIG_MYCELIUM = new SurfaceBuilderBaseConfiguration(
		EdenBlocks.EDEN_MYCELIUM.defaultBlockState(),
		Blocks.DIRT.defaultBlockState(),
		Blocks.DIRT.defaultBlockState()
	);
	private static final ConfiguredSurfaceBuilder DEFAULT_BUILDER = registerSurf("default_grass", SurfaceBuilder.DEFAULT.configured(CONFIG_GRASS));
	private static final ConfiguredSurfaceBuilder MYCELIUM_BUILDER = registerSurf("mycelium", SurfaceBuilder.DEFAULT.configured(CONFIG_MYCELIUM));*/
	
	// LAND //
	public static final BCLBiome STONE_GARDEN = registerLand(makeStoneGardenBiome());
	// TODO make all biomes
	public static final BCLBiome GOLDEN_FOREST = registerLand(makeStoneGardenBiome());
	public static final BCLBiome MYCOTIC_FOREST = registerLand(makeStoneGardenBiome());
	public static final BCLBiome PULSE_FOREST = registerLand(makeStoneGardenBiome());
	public static final BCLBiome BRAINSTORM = registerLand(makeStoneGardenBiome());
	public static final BCLBiome LAKESIDE_DESERT = registerLand(makeStoneGardenBiome());
	public static final BCLBiome WIND_VALLEY = registerLand(makeStoneGardenBiome());
	
	// CAVES //
	public static final BCLBiome ERODED_CAVE = registerCave(makeStoneGardenBiome());
	
	public static void init() {
		CONFIG.saveChanges();
	}
	
	private static BCLBiome registerLand(BCLBiome biome) {
		BIOMES_LAND.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	private static BCLBiome registerCave(BCLBiome biome) {
		BIOMES_CAVE.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	private static BCLBiomeBuilder addDefaultFeatures(BCLBiomeBuilder builder) {
		return builder
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
			.feature(EdenFeatures.GRAVILITE_CRYSTAL)
			.feature(EdenFeatures.ROUND_CAVE)
			.carver(Carving.AIR, Carvers.CAVE);
	}
	
	private static BCLBiomeBuilder addDefaultSurface(BCLBiomeBuilder builder) {
		return builder.surface(SurfaceRuleBuilder
			.start()
			.surface(Blocks.GRASS_BLOCK.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
	}
	
	private static BCLBiome makeStoneGardenBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("stone_garden"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		return builder
			.skyColor(113, 178, 255)
			.fogColor(183, 212, 255)
			.plantsColor(162, 190, 113)
			.waterColor(4159204)
			.waterFogColor(329011)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.FOREST_ROCK)
			.feature(EdenFeatures.COBBLE_FLOOR)
			.feature(EdenFeatures.MOSS_FLOOR)
			.feature(EdenFeatures.MOSS_LAYER)
			.feature(EdenFeatures.EDEN_MOSS_LAYER)
			.feature(EdenFeatures.STONE_PILLAR)
			.feature(EdenFeatures.VIOLUM_RARE)
			.feature(EdenFeatures.EDEN_VINE)
			.feature(EdenFeatures.ROOTS)
			.build();
	}
}
