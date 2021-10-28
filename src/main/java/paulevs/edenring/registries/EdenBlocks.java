package paulevs.edenring.registries;


import net.minecraft.world.level.block.Block;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.EdenGrassBlock;
import paulevs.edenring.blocks.MossyStoneBlock;
import ru.bclib.config.PathConfig;
import ru.bclib.registry.BlockRegistry;

public class EdenBlocks {
	public static final BlockRegistry REGISTRY = new BlockRegistry(EdenRing.EDEN_TAB, new PathConfig(EdenRing.MOD_ID, "blocks"));
	
	public static final Block EDEN_GRASS_BLOCK = register("eden_grass", new EdenGrassBlock());
	public static final Block MOSSY_STONE = register("mossy_stone", new MossyStoneBlock());
	
	public static void init() {}
	
	private static Block register(String name, Block block) {
		return REGISTRY.register(EdenRing.makeID(name), block);
	}
}
