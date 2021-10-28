package paulevs.edenring.world.structures;

import net.minecraft.world.level.block.Blocks;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.world.features.BCLFeature;

public class EdenFeatures {
	public static final BCLFeature MOSS_LAYER = BCLFeature.makeVegetationFeature(EdenRing.makeID("moss_layer"), new ScatterFeature(Blocks.MOSS_CARPET), 4);
	public static final BCLFeature MOSS_FLOOR = BCLFeature.makeVegetationFeature(
		EdenRing.makeID("moss_floor"),
		new FloorScatterFeature(Blocks.MOSS_BLOCK, EdenBlocks.EDEN_GRASS_BLOCK), 4
	);
	public static final BCLFeature COBBLE_FLOOR = BCLFeature.makeVegetationFeature(
		EdenRing.makeID("cobble_floor"),
		new FloorScatterFeature(Blocks.MOSSY_COBBLESTONE, EdenBlocks.EDEN_GRASS_BLOCK), 4
	);
	public static final BCLFeature STONE_PILLAR = BCLFeature.makeRawGenFeature(EdenRing.makeID("stone_pillar"), new StonePillar(), 15);
	
	public static final BCLFeature SLATE_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("slate_layer"), new StoneLayer(Blocks.DEEPSLATE));
	public static final BCLFeature CALCITE_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("calcite_layer"), new StoneLayer(Blocks.CALCITE));
	public static final BCLFeature TUFF_LAYER = BCLFeature.makeChunkFeature(EdenRing.makeID("tuff_layer"), new StoneLayer(Blocks.TUFF));
	
	public static void init() {}
}
