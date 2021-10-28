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
	
	public static void init() {}
}
