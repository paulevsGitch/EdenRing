package paulevs.edenring.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.SixSidePlant;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.features.basic.DepthScatterFeature;
import paulevs.edenring.world.features.basic.DoubleScatterFeature;
import paulevs.edenring.world.features.basic.FloorScatterFeature;
import paulevs.edenring.world.features.basic.ScatterFeature;
import paulevs.edenring.world.features.basic.SixSideScatter;
import paulevs.edenring.world.features.plants.AquatusFeature;
import paulevs.edenring.world.features.plants.LimphiumFeature;
import paulevs.edenring.world.features.plants.RootsFeature;
import paulevs.edenring.world.features.plants.TallMushroomFeature;
import paulevs.edenring.world.features.plants.VineFeature;
import paulevs.edenring.world.features.plants.VolvoxFeature;
import paulevs.edenring.world.features.terrain.GraviliteCrystalFeature;
import paulevs.edenring.world.features.terrain.LayeredBulbFeature;
import paulevs.edenring.world.features.terrain.SmallIslandFeature;
import paulevs.edenring.world.features.terrain.StoneLayer;
import paulevs.edenring.world.features.terrain.StonePillar;
import paulevs.edenring.world.features.trees.AuritisTreeFeature;
import paulevs.edenring.world.features.trees.BalloonMushroomTreeFeature;
import paulevs.edenring.world.features.trees.BrainTreeFeature;
import paulevs.edenring.world.features.trees.OldBalloonMushroomTreeFeature;
import paulevs.edenring.world.features.trees.PulseTreeFeature;

public class EdenFeatures {
	public static final StonePillar STONE_PILLAR_FEATURE = inlineBuild("stone_pillar", new StonePillar());

	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> MOSS_LAYER = registerVegetation("moss_layer", inlineBuild("moss_layer", new ScatterFeature(Blocks.MOSS_CARPET)), 4);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> EDEN_MOSS_LAYER = registerVegetation("eden_moss_layer", inlineBuild("eden_moss_layer", new ScatterFeature(EdenBlocks.EDEN_MOSS)), 6);
	
	public static final BCLFeature<FloorScatterFeature, NoneFeatureConfiguration> MOSS_FLOOR = registerVegetation(
		"moss_floor",
		inlineBuild("moss_floor", new FloorScatterFeature(Blocks.MOSS_BLOCK, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT)), 16
	);
	public static final BCLFeature<FloorScatterFeature, NoneFeatureConfiguration> COBBLE_FLOOR = registerVegetation(
		"cobble_floor",
		inlineBuild("cobble_floor", new FloorScatterFeature(Blocks.MOSSY_COBBLESTONE, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT)), 8
	);
	public static final BCLFeature<FloorScatterFeature, NoneFeatureConfiguration> GRASS_FLOOR = registerVegetation(
		"grass_floor",
		inlineBuild("grass_floor", new FloorScatterFeature(EdenBlocks.EDEN_GRASS_BLOCK, true, EdenBlocks.EDEN_MYCELIUM, Blocks.SAND)), 6
	);
	public static final BCLFeature<FloorScatterFeature, NoneFeatureConfiguration> GRAVEL_FLOOR = registerVegetation(
		"gravel_floor",
		inlineBuild("gravel_floor", new FloorScatterFeature(Blocks.GRAVEL, Blocks.SAND)), 6
	);
	
	public static final BCLFeature<StonePillar, NoneFeatureConfiguration> STONE_PILLAR = registerRawGen("stone_pillar", STONE_PILLAR_FEATURE, 15);
	
	public static final BCLFeature<StoneLayer, NoneFeatureConfiguration> SLATE_LAYER = registerChunk("slate_layer", inlineBuild("slate_layer", new StoneLayer(Blocks.DEEPSLATE)), NoneFeatureConfiguration.NONE);
	public static final BCLFeature<StoneLayer, NoneFeatureConfiguration> CALCITE_LAYER = registerChunk("calcite_layer", inlineBuild("calcite_layer", new StoneLayer(Blocks.CALCITE)), NoneFeatureConfiguration.NONE);
	public static final BCLFeature<StoneLayer, NoneFeatureConfiguration> TUFF_LAYER = registerChunk("tuff_layer", inlineBuild("tuff_layer", new StoneLayer(Blocks.TUFF)), NoneFeatureConfiguration.NONE);
	
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_MOSSY_COBBLE = registerChunk(
		"ore_mossy_cobble",
		inlineBuild("ore_mossy_cobble", new DepthScatterFeature(Blocks.MOSSY_COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_COBBLE = registerChunk(
		"ore_cobble",
		inlineBuild("ore_cobble", new DepthScatterFeature(Blocks.COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_COAL = registerChunk(
		"ore_coal",
		inlineBuild("ore_coal", new DepthScatterFeature(Blocks.COAL_ORE, 20, 5, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_IRON = registerChunk(
		"ore_iron",
		inlineBuild("ore_iron", new DepthScatterFeature(Blocks.IRON_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_COPPER = registerChunk(
		"ore_copper",
		inlineBuild("ore_copper", new DepthScatterFeature(Blocks.COPPER_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<DepthScatterFeature, NoneFeatureConfiguration> ORE_GOLD = registerChunk(
		"ore_gold",
		inlineBuild("ore_gold", new DepthScatterFeature(Blocks.GOLD_ORE, 8, 2, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	
	public static final BCLFeature<LayeredBulbFeature, NoneFeatureConfiguration> LAYERED_IRON = registerChunk(
		"layered_iron",
		inlineBuild("layered_iron", new LayeredBulbFeature(new Block[] { Blocks.RAW_IRON_BLOCK, Blocks.IRON_ORE }, 32, 6, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<LayeredBulbFeature, NoneFeatureConfiguration> LAYERED_COPPER = registerChunk(
		"layered_copper",
		inlineBuild("layered_copper", new LayeredBulbFeature(new Block[] { Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_ORE }, 32, 6, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
		NoneFeatureConfiguration.NONE
	);
	public static final BCLFeature<LayeredBulbFeature, NoneFeatureConfiguration> LAYERED_GOLD = registerChunk(
		"layered_gold",
		inlineBuild("layered_gold", new LayeredBulbFeature(new Block[] { Blocks.RAW_GOLD_BLOCK, Blocks.GOLD_ORE }, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)),
			NoneFeatureConfiguration.NONE
	);
	
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> MYCOTIC_GRASS = registerVegetation("mycotic_grass", inlineBuild("mycotic_grass", new ScatterFeature(EdenBlocks.MYCOTIC_GRASS)), 12);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> GOLDEN_GRASS = registerVegetation("golden_grass", inlineBuild("golden_grass", new ScatterFeature(EdenBlocks.GOLDEN_GRASS)), 8);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> BALLOON_MUSHROOM_SMALL = registerVegetation("balloon_mushroom_small", inlineBuild("balloon_mushroom_small", new ScatterFeature(EdenBlocks.BALLOON_MUSHROOM_SMALL)), 6);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> COPPER_GRASS = registerVegetation("copper_grass", inlineBuild("copper_grass", new ScatterFeature(EdenBlocks.COPPER_GRASS)), 3);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> IRON_GRASS = registerVegetation("iron_grass", inlineBuild("iron_grass", new ScatterFeature(EdenBlocks.IRON_GRASS)), 3);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> GOLD_GRASS = registerVegetation("gold_grass", inlineBuild("gold_grass", new ScatterFeature(EdenBlocks.GOLD_GRASS)), 3);
	public static final BCLFeature<ScatterFeature, NoneFeatureConfiguration> LONLIX = registerVegetation("lonlix", inlineBuild("lonlix", new ScatterFeature(EdenBlocks.LONLIX)), 3);
	
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> VIOLUM_DENSE = registerVegetation("violum_dense", inlineBuild("violum_dense", new DoubleScatterFeature(EdenBlocks.VIOLUM)), 8);
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> VIOLUM_RARE = registerVegetation("violum_rare", inlineBuild("violum_rare", new DoubleScatterFeature(EdenBlocks.VIOLUM)), 1);
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> TALL_MYCOTIC_GRASS = registerVegetation("tall_mycotic_grass", inlineBuild("tall_mycotic_grass", new DoubleScatterFeature(EdenBlocks.TALL_MYCOTIC_GRASS, 8)), 6);
	public static final BCLFeature<LimphiumFeature, NoneFeatureConfiguration> LIMPHIUM = registerChanced("limphium", Decoration.VEGETAL_DECORATION, inlineBuild("limphium", new LimphiumFeature()), NoneFeatureConfiguration.NONE, 8);
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> TALL_COPPER_GRASS = registerVegetation("tall_copper_grass", inlineBuild("tall_copper_grass", new DoubleScatterFeature(EdenBlocks.TALL_COPPER_GRASS, 6)), 4);
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> TALL_IRON_GRASS = registerVegetation("tall_iron_grass", inlineBuild("tall_iron_grass", new DoubleScatterFeature(EdenBlocks.TALL_IRON_GRASS, 6)), 4);
	public static final BCLFeature<DoubleScatterFeature, NoneFeatureConfiguration> TALL_GOLD_GRASS = registerVegetation("tall_gold_grass", inlineBuild("tall_gold_grass", new DoubleScatterFeature(EdenBlocks.TALL_GOLD_GRASS, 6)), 4);
	
	public static final BCLFeature<BalloonMushroomTreeFeature, NoneFeatureConfiguration> BALLOON_MUSHROOM_TREE = registerVegetation("balloon_mushroom_tree", inlineBuild("balloon_mushroom_tree", new BalloonMushroomTreeFeature()), 16);
	public static final BCLFeature<OldBalloonMushroomTreeFeature, NoneFeatureConfiguration> OLD_BALLOON_MUSHROOM_TREE = registerVegetation("old_balloon_mushroom_tree", inlineBuild("old_balloon_mushroom_tree", new OldBalloonMushroomTreeFeature()), 3);
	public static final BCLFeature<AuritisTreeFeature, NoneFeatureConfiguration> AURITIS_TREE = registerVegetation("auritis_tree", inlineBuild("auritis_tree", new AuritisTreeFeature()), 10);
	public static final BCLFeature<PulseTreeFeature, NoneFeatureConfiguration> PULSE_TREE = registerVegetation("pulse_tree", inlineBuild("pulse_tree", new PulseTreeFeature()), 50);
	public static final BCLFeature<BrainTreeFeature, NoneFeatureConfiguration> BRAIN_TREE = registerVegetation("brain_tree", inlineBuild("brain_tree", new BrainTreeFeature()), 8);
	public static final BCLFeature<AquatusFeature, NoneFeatureConfiguration> AQUATUS = registerVegetation("aquatus", inlineBuild("aquatus", new AquatusFeature()), 8);
	public static final BCLFeature<VolvoxFeature, NoneFeatureConfiguration> VOLVOX = registerChanced("volvox", Decoration.VEGETAL_DECORATION, inlineBuild("volvox", new VolvoxFeature()), NoneFeatureConfiguration.NONE, 3);
	
	public static final BCLFeature<VineFeature, NoneFeatureConfiguration> EDEN_VINE = registerVegetation("eden_vine", inlineBuild("eden_vine", new VineFeature()), 2);
	public static final BCLFeature<RootsFeature, NoneFeatureConfiguration> ROOTS = registerVegetation("roots", inlineBuild("roots", new RootsFeature()), 4);
	
	public static final BCLFeature<SixSideScatter, NoneFeatureConfiguration> PARIGNUM = registerVegetation("parignum", inlineBuild("parignum", new SixSideScatter((SixSidePlant) EdenBlocks.PARIGNUM)), 8);
	public static final BCLFeature<TallMushroomFeature, NoneFeatureConfiguration> TALL_BALLOON_MUSHROOM = registerVegetation("tall_balloon_mushroom", inlineBuild("tall_balloon_mushroom", new TallMushroomFeature()), 6);
	
	public static final BCLFeature<GraviliteCrystalFeature, NoneFeatureConfiguration> GRAVILITE_CRYSTAL = registerRawGen("gravilite_crystal", inlineBuild("gravilite_crystal", new GraviliteCrystalFeature()), 100);
	public static final BCLFeature<SmallIslandFeature, NoneFeatureConfiguration> SMALL_ISLAND = registerRawGen("small_island", inlineBuild("small_island", new SmallIslandFeature()), 50);

	public static <F extends Feature<FC>, FC extends FeatureConfiguration> F inlineBuild(String name, F feature) {
		ResourceLocation l = EdenRing.makeID(name);
		if (BuiltInRegistries.FEATURE.containsKey(l)) {
			return (F) BuiltInRegistries.FEATURE.get(l);
		}
		return BCLFeature.register(l, feature);
	}

	private static <F extends Feature<NoneFeatureConfiguration>> BCLFeature<F, NoneFeatureConfiguration> registerVegetation(
			String name,
			F feature,
			int density
	) {
		return registerVegetation(name, feature, NoneFeatureConfiguration.NONE, density);
	}

	private static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerVegetation(
			String name,
			F feature,
			FC config,
			int density
	) {
		ResourceLocation id = EdenRing.makeID(name);
		return BCLFeatureBuilder.start(id, feature)
				.configuration(config)
				.build()
				.place()
				.onEveryLayerMax(density)
				.onlyInBiome()
				.build();
	}

	private static <F extends Feature<NoneFeatureConfiguration>> BCLFeature<F, NoneFeatureConfiguration> registerChanced(
			String name,
			F feature,
			int density
	) {
		return registerChanced(name, feature, NoneFeatureConfiguration.NONE, density);
	}

	private static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerChanced(
			String name,
			F feature,
			FC config,
			int chance
	) {
		return registerChanced(name, Decoration.SURFACE_STRUCTURES, feature, config, chance);
	}

	private static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerChanced(
			String name,
			Decoration decoration,
			F feature,
			FC config,
			int chance
	) {
		return
				BCLFeatureBuilder
						.start(EdenRing.makeID(name), feature)
						.configuration(config)
						.build()
						.place()
						.decoration(decoration)
						.onceEvery(chance)
						.squarePlacement()
						.onlyInBiome()
						.build();
	}

	private static <F extends Feature<NoneFeatureConfiguration>> BCLFeature<F, NoneFeatureConfiguration> registerRawGen(
			String name,
			F feature,
			int density
	) {
		return registerRawGen(name, feature, NoneFeatureConfiguration.NONE, density);
	}

	private static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerRawGen(
			String name,
			F feature,
			FC config,
			int chance
	) {
		return registerChanced(name, Decoration.RAW_GENERATION, feature, config, chance);
	}

	public static <F extends Feature<FC>, FC extends FeatureConfiguration> BCLFeature<F, FC> registerChunk(
			String name,
			F feature,
			FC config
	) {
		return BCLFeatureBuilder
				.start(EdenRing.makeID(name), feature)
				.configuration(config)
				.build()
				.place()
				.decoration(Decoration.UNDERGROUND_DECORATION)
				.count(1)
				.onlyInBiome()
				.build();
	}

	public static void register() {
	}
}
