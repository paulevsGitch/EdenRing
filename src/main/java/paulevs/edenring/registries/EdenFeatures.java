package paulevs.edenring.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.SixSidePlant;
import paulevs.edenring.world.features.basic.DepthScatterFeature;
import paulevs.edenring.world.features.basic.DoubleScatterFeature;
import paulevs.edenring.world.features.basic.FloorScatterFeature;
import paulevs.edenring.world.features.basic.ScatterFeature;
import paulevs.edenring.world.features.basic.SixSideScatter;
import paulevs.edenring.world.features.plants.AquatusFeature;
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
import ru.bclib.api.features.BCLCommonFeatures;
import ru.bclib.world.features.BCLFeature;

public class EdenFeatures {
	public static final BCLFeature MOSS_LAYER = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("moss_layer"), new ScatterFeature(Blocks.MOSS_CARPET), 4);
	public static final BCLFeature EDEN_MOSS_LAYER = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("eden_moss_layer"), new ScatterFeature(EdenBlocks.EDEN_MOSS), 6);
	
	public static final BCLFeature MOSS_FLOOR = BCLCommonFeatures.makeVegetationFeature(
		EdenRing.makeID("moss_floor"),
		new FloorScatterFeature(Blocks.MOSS_BLOCK, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT), 16
	);
	public static final BCLFeature COBBLE_FLOOR = BCLCommonFeatures.makeVegetationFeature(
		EdenRing.makeID("cobble_floor"),
		new FloorScatterFeature(Blocks.MOSSY_COBBLESTONE, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT), 8
	);
	public static final BCLFeature GRASS_FLOOR = BCLCommonFeatures.makeVegetationFeature(
		EdenRing.makeID("grass_floor"),
		new FloorScatterFeature(EdenBlocks.EDEN_GRASS_BLOCK, true, EdenBlocks.EDEN_MYCELIUM, Blocks.SAND), 6
	);
	public static final BCLFeature GRAVEL_FLOOR = BCLCommonFeatures.makeVegetationFeature(
		EdenRing.makeID("gravel_floor"),
		new FloorScatterFeature(Blocks.GRAVEL, Blocks.SAND), 6
	);
	
	public static final BCLFeature STONE_PILLAR = BCLCommonFeatures.makeChancedFeature(EdenRing.makeID("stone_pillar"), Decoration.RAW_GENERATION, new StonePillar(), 15);
	
	public static final BCLFeature SLATE_LAYER = BCLCommonFeatures.makeChunkFeature(EdenRing.makeID("slate_layer"), Decoration.UNDERGROUND_DECORATION, new StoneLayer(Blocks.DEEPSLATE));
	public static final BCLFeature CALCITE_LAYER = BCLCommonFeatures.makeChunkFeature(EdenRing.makeID("calcite_layer"), Decoration.UNDERGROUND_DECORATION, new StoneLayer(Blocks.CALCITE));
	public static final BCLFeature TUFF_LAYER = BCLCommonFeatures.makeChunkFeature(EdenRing.makeID("tuff_layer"), Decoration.UNDERGROUND_DECORATION, new StoneLayer(Blocks.TUFF));
	
	public static final BCLFeature ORE_MOSSY_COBBLE = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_mossy_cobble"),
		Decoration.UNDERGROUND_DECORATION,
		new DepthScatterFeature(Blocks.MOSSY_COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature ORE_COBBLE = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_cobble"),
		Decoration.UNDERGROUND_DECORATION,
		new DepthScatterFeature(Blocks.COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature ORE_COAL = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_coal"),
		Decoration.UNDERGROUND_DECORATION,
		new DepthScatterFeature(Blocks.COAL_ORE, 20, 5, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature ORE_IRON = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_iron"),
		Decoration.UNDERGROUND_DECORATION,
		new DepthScatterFeature(Blocks.IRON_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature ORE_COPPER = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_copper"),
		Decoration.UNDERGROUND_DECORATION, new DepthScatterFeature(Blocks.COPPER_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature ORE_GOLD = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("ore_gold"),
		Decoration.UNDERGROUND_DECORATION,
		new DepthScatterFeature(Blocks.GOLD_ORE, 8, 2, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	
	public static final BCLFeature LAYERED_IRON = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("layered_iron"),
		Decoration.UNDERGROUND_DECORATION,
		new LayeredBulbFeature(new Block[] { Blocks.RAW_IRON_BLOCK, Blocks.IRON_ORE }, 32, 6, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature LAYERED_COPPER = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("layered_copper"),
		Decoration.UNDERGROUND_DECORATION,
		new LayeredBulbFeature(new Block[] { Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_ORE }, 32, 6, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	public static final BCLFeature LAYERED_GOLD = BCLCommonFeatures.makeChunkFeature(
		EdenRing.makeID("layered_gold"),
		Decoration.UNDERGROUND_DECORATION,
		new LayeredBulbFeature(new Block[] { Blocks.RAW_GOLD_BLOCK, Blocks.GOLD_ORE }, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF)
	);
	
	public static final BCLFeature MYCOTIC_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("mycotic_grass"), new ScatterFeature(EdenBlocks.MYCOTIC_GRASS), 12);
	public static final BCLFeature GOLDEN_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("golden_grass"), new ScatterFeature(EdenBlocks.GOLDEN_GRASS), 8);
	public static final BCLFeature BALLOON_MUSHROOM_SMALL = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("balloon_mushroom_small"), new ScatterFeature(EdenBlocks.BALLOON_MUSHROOM_SMALL), 6);
	public static final BCLFeature IRON_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("iron_grass"), new ScatterFeature(EdenBlocks.IRON_GRASS), 3);
	public static final BCLFeature COPPER_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("copper_grass"), new ScatterFeature(EdenBlocks.COPPER_GRASS), 3);
	public static final BCLFeature GOLD_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("gold_grass"), new ScatterFeature(EdenBlocks.GOLD_GRASS), 3);
	public static final BCLFeature LONLIX = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("lonlix"), new ScatterFeature(EdenBlocks.LONLIX), 3);
	
	public static final BCLFeature VIOLUM_DENSE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("violum_dense"), new DoubleScatterFeature(EdenBlocks.VIOLUM), 8);
	public static final BCLFeature VIOLUM_RARE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("violum_rare"), new DoubleScatterFeature(EdenBlocks.VIOLUM), 1);
	public static final BCLFeature TALL_MYCOTIC_GRASS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("tall_mycotic_grass"), new DoubleScatterFeature(EdenBlocks.TALL_MYCOTIC_GRASS, 8), 6);
	
	public static final BCLFeature BALLOON_MUSHROOM_TREE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("balloon_mushroom_tree"), new BalloonMushroomTreeFeature(), 16);
	public static final BCLFeature OLD_BALLOON_MUSHROOM_TREE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("old_balloon_mushroom_tree"), new OldBalloonMushroomTreeFeature(), 3);
	public static final BCLFeature AURITIS_TREE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("auritis_tree"), new AuritisTreeFeature(), 10);
	public static final BCLFeature PULSE_TREE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("pulse_tree"), new PulseTreeFeature(), 50);
	public static final BCLFeature BRAIN_TREE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("brain_tree"), new BrainTreeFeature(), 8);
	public static final BCLFeature AQUATUS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("aquatus"), new AquatusFeature(), 8);
	public static final BCLFeature VOLVOX = BCLCommonFeatures.makeChancedFeature(EdenRing.makeID("volvox"), Decoration.VEGETAL_DECORATION, new VolvoxFeature(), 3);
	
	public static final BCLFeature EDEN_VINE = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("eden_vine"), new VineFeature(), 2);
	public static final BCLFeature ROOTS = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("roots"), new RootsFeature(), 4);
	
	public static final BCLFeature PARIGNUM = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("parignum"), new SixSideScatter((SixSidePlant) EdenBlocks.PARIGNUM), 8);
	public static final BCLFeature TALL_BALLOON_MUSHROOM = BCLCommonFeatures.makeVegetationFeature(EdenRing.makeID("tall_balloon_mushroom"), new TallMushroomFeature(), 6);
	
	public static final BCLFeature GRAVILITE_CRYSTAL = BCLCommonFeatures.makeChancedFeature(EdenRing.makeID("gravilite_crystal"), Decoration.RAW_GENERATION, new GraviliteCrystalFeature(), 100);
	public static final BCLFeature SMALL_ISLAND = BCLCommonFeatures.makeChancedFeature(EdenRing.makeID("small_island"), Decoration.RAW_GENERATION, new SmallIslandFeature(), 50);
	
	public static void init() {}
}
