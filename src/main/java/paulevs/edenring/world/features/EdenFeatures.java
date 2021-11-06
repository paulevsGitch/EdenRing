package paulevs.edenring.world.features;

import net.minecraft.world.level.block.Blocks;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.world.features.BCLFeature;

public class EdenFeatures {
	public static final BCLFeature MOSS_LAYER = BCLFeature.makeVegetationFeature(EdenRing.makeID("moss_layer"), new ScatterFeature(Blocks.MOSS_CARPET), 4);
	public static final BCLFeature EDEN_MOSS_LAYER = BCLFeature.makeVegetationFeature(EdenRing.makeID("eden_moss_layer"), new ScatterFeature(EdenBlocks.EDEN_MOSS), 6);
	
	public static final BCLFeature MOSS_FLOOR = BCLFeature.makeVegetationFeature(
		EdenRing.makeID("moss_floor"),
		new FloorScatterFeature(Blocks.MOSS_BLOCK, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT), 16
	);
	public static final BCLFeature COBBLE_FLOOR = BCLFeature.makeVegetationFeature(
		EdenRing.makeID("cobble_floor"),
		new FloorScatterFeature(Blocks.MOSSY_COBBLESTONE, EdenBlocks.EDEN_GRASS_BLOCK, Blocks.DIRT), 8
	);
	public static final BCLFeature GRASS_FLOOR = BCLFeature.makeVegetationFeature(
		EdenRing.makeID("grass_floor"),
		new FloorScatterFeature(EdenBlocks.EDEN_GRASS_BLOCK, EdenBlocks.EDEN_MYCELIUM), 6
	);
	public static final BCLFeature STONE_PILLAR = BCLFeature.makeRawGenFeature(EdenRing.makeID("stone_pillar"), new StonePillar(), 15);
	
	public static final BCLFeature SLATE_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("slate_layer"), new StoneLayer(Blocks.DEEPSLATE));
	public static final BCLFeature CALCITE_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("calcite_layer"), new StoneLayer(Blocks.CALCITE));
	public static final BCLFeature TUFF_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("tuff_layer"), new StoneLayer(Blocks.TUFF));
	
	public static final BCLFeature ORE_MOSSY_COBBLE = BCLFeature.makeChunkFeature(EdenRing.makeID("ore_mossy_cobble"), new DepthScatterFeature(
		Blocks.MOSSY_COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF
	));
	public static final BCLFeature ORE_COBBLE = BCLFeature.makeChunkFeature(EdenRing.makeID("ore_cobble"), new DepthScatterFeature(
		Blocks.COBBLESTONE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF
	));
	public static final BCLFeature ORE_COAL = BCLFeature.makeChunkFeature(EdenRing.makeID("ore_coal"), new DepthScatterFeature(
		Blocks.COAL_ORE, 20, 5, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF
	));
	public static final BCLFeature ORE_IRON = BCLFeature.makeChunkFeature(EdenRing.makeID("ore_iron"), new DepthScatterFeature(
		Blocks.IRON_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF
	));
	public static final BCLFeature ORE_COPPER = BCLFeature.makeChunkFeature(EdenRing.makeID("ore_copper"), new DepthScatterFeature(
		Blocks.COPPER_ORE, 16, 4, Blocks.STONE, Blocks.DEEPSLATE, Blocks.CALCITE, Blocks.TUFF
	));
	
	public static final BCLFeature MYCOTIC_GRASS = BCLFeature.makeVegetationFeature(EdenRing.makeID("mycotic_grass"), new ScatterFeature(EdenBlocks.MYCOTIC_GRASS), 12);
	public static final BCLFeature GOLDEN_GRASS = BCLFeature.makeVegetationFeature(EdenRing.makeID("golden_grass"), new ScatterFeature(EdenBlocks.GOLDEN_GRASS), 8);
	public static final BCLFeature BALLOON_MUSHROOM_SMALL = BCLFeature.makeVegetationFeature(EdenRing.makeID("balloon_mushroom_small"), new ScatterFeature(EdenBlocks.BALLOON_MUSHROOM_SMALL), 6);
	
	public static final BCLFeature VIOLUM_DENSE = BCLFeature.makeVegetationFeature(EdenRing.makeID("violum_dense"), new DoubleScatterFeature(EdenBlocks.VIOLUM), 8);
	public static final BCLFeature VIOLUM_RARE = BCLFeature.makeVegetationFeature(EdenRing.makeID("violum_rare"), new DoubleScatterFeature(EdenBlocks.VIOLUM), 1);
	
	public static final BCLFeature BALLOON_MUSHROOM_TREE = BCLFeature.makeVegetationFeature(EdenRing.makeID("balloon_mushroom_tree"), new BalloonMushroomTreeFeature(), 12);
	public static final BCLFeature AURITIS_TREE = BCLFeature.makeVegetationFeature(EdenRing.makeID("auritis_tree"), new AuritisTreeFeature(), 10);
	public static final BCLFeature PULSE_TREE = BCLFeature.makeVegetationFeature(EdenRing.makeID("pulse_tree"), new PulseTreeFeature(), 50);
	public static final BCLFeature BRAIN_TREE = BCLFeature.makeVegetationFeature(EdenRing.makeID("brain_tree"), new BrainTreeFeature(), 8);
	
	public static final BCLFeature EDEN_VINE = BCLFeature.makeVegetationFeature(EdenRing.makeID("eden_vine"), new VineFeature(), 2);
	public static final BCLFeature ROOTS = BCLFeature.makeVegetationFeature(EdenRing.makeID("roots"), new RootsFeature(), 4);
	
	public static void init() {}
}
