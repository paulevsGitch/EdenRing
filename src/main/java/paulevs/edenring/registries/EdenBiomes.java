package paulevs.edenring.registries;

import com.google.common.collect.Lists;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
	
	// LAND //
	public static final BCLBiome STONE_GARDEN = registerLand(makeStoneGardenBiome());
	public static final BCLBiome GOLDEN_FOREST = registerLand(makeGoldenForestBiome());
	public static final BCLBiome MYCOTIC_FOREST = registerLand(makeMycoticForestBiome());
	public static final BCLBiome PULSE_FOREST = registerLand(makePulseForestBiome());
	public static final BCLBiome BRAINSTORM = registerLand(makeBrainstormBiome());
	public static final BCLBiome LAKESIDE_DESERT = registerLand(makeLakesideDesertBiome());
	public static final BCLBiome WIND_VALLEY = registerLand(makeWindValleyBiome());
	
	// CAVES //
	//public static final BCLBiome ERODED_CAVE = registerCave(makeStoneGardenBiome());
	
	public static void init() {
		CONFIG.saveChanges();
	}
	
	private static BCLBiome registerLand(BCLBiome biome) {
		BIOMES_LAND.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	/*private static BCLBiome registerCave(BCLBiome biome) {
		BIOMES_CAVE.add(biome);
		return BiomeAPI.registerBiome(biome);
	}*/
	
	private static void addDefaultFeatures(BCLBiomeBuilder builder) {
		builder
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
	
	private static void addDefaultSurface(BCLBiomeBuilder builder) {
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(Blocks.GRASS_BLOCK.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
	}
	
	private static void setDefaultColors(BCLBiomeBuilder builder) {
		builder.skyColor(113, 178, 255).fogColor(183, 212, 255).waterFogColor(329011).waterColor(4159204);
	}
	
	private static BCLBiome makeStoneGardenBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("stone_garden"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		setDefaultColors(builder);
		return builder
			.plantsColor(162, 190, 113)
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
	
	private static BCLBiome makeGoldenForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("golden_forest"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		setDefaultColors(builder);
		return builder
			.plantsColor(255, 174, 100)
			.skyColor(113, 178, 255)
			.fogColor(183, 212, 255)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
			.feature(EdenFeatures.AURITIS_TREE)
			.feature(EdenFeatures.EDEN_MOSS_LAYER)
			.feature(EdenFeatures.GOLDEN_GRASS)
			.feature(EdenFeatures.EDEN_VINE)
			.feature(EdenFeatures.ORE_GOLD)
			.build();
	}
	
	private static BCLBiome makeMycoticForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("mycotic_forest"));
		addDefaultFeatures(builder);
		setDefaultColors(builder);
		
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(EdenBlocks.MYCOTIC_GRASS.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
		
		return builder
			.grassColor(220, 130, 189)
			.foliageColor(152, 90, 131)
			.skyColor(113, 178, 255)
			.fogColor(178, 112, 143)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(EdenFeatures.MYCOTIC_GRASS)
			.feature(EdenFeatures.BALLOON_MUSHROOM_TREE)
			.feature(EdenFeatures.BALLOON_MUSHROOM_SMALL)
			.feature(EdenFeatures.GRASS_FLOOR)
			.feature(EdenFeatures.EDEN_VINE)
			.build();
	}
	
	private static BCLBiome makePulseForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("pulse_forest"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		setDefaultColors(builder);
		return builder
			.skyColor(113, 178, 255)
			.fogColor(115, 235, 242)
			.plantsColor(121, 238, 248)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
			.feature(EdenFeatures.PULSE_TREE)
			.feature(EdenFeatures.VIOLUM_DENSE)
			.feature(EdenFeatures.EDEN_VINE)
			.build();
	}
	
	private static BCLBiome makeBrainstormBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("brainstorm"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		setDefaultColors(builder);
		return builder
			.fogDensity(2.5F)
			.skyColor(113, 178, 255)
			.fogColor(180, 180, 180)
			.plantsColor(200, 200, 200)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(EdenFeatures.IRON_GRASS)
			.feature(EdenFeatures.COPPER_GRASS)
			.feature(EdenFeatures.GOLD_GRASS)
			.feature(EdenFeatures.BRAIN_TREE)
			.feature(EdenFeatures.LAYERED_IRON)
			.feature(EdenFeatures.LAYERED_COPPER)
			.feature(EdenFeatures.LAYERED_GOLD)
			.build();
	}
	
	private static BCLBiome makeLakesideDesertBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("lakeside_desert"));
		addDefaultFeatures(builder);
		setDefaultColors(builder);
		
		BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
		builder.surface(SurfaceRuleBuilder
			.start()
			.subsurface(Blocks.SAND.defaultBlockState(), 3)
			.filler(sandstone)
			.ceil(sandstone)
			.build()
		);
		
		return builder
			.fogDensity(2.5F)
			.skyColor(113, 178, 255)
			.fogColor(237, 235, 203)
			.grassColor(246, 222, 173)
			.foliageColor(247, 165, 115)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
			.feature(EdenFeatures.GRAVEL_FLOOR)
			.feature(EdenFeatures.GRASS_FLOOR)
			.feature(EdenFeatures.AQUATUS)
			.build();
	}
	
	private static BCLBiome makeWindValleyBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("wind_valley"));
		addDefaultFeatures(builder);
		addDefaultSurface(builder);
		setDefaultColors(builder);
		return builder
			.fogDensity(2.5F)
			.skyColor(113, 178, 255)
			.fogColor(183, 212, 255)
			.grassColor(225, 84, 72)
			.foliageColor(230, 63, 50)
			.particles(EdenParticles.WIND_PARTICLE, 0.001F)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(EdenFeatures.VIOLUM_RARE)
			.feature(EdenFeatures.LONLIX)
			.build();
	}
}
