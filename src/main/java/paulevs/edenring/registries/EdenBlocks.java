package paulevs.edenring.registries;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.BaloonMushroomBlock;
import paulevs.edenring.blocks.BaloonMushroomSmallBlock;
import paulevs.edenring.blocks.BaloonMushroomStemBlock;
import paulevs.edenring.blocks.EdenGrassBlock;
import paulevs.edenring.blocks.SimplePlantBlock;
import paulevs.edenring.blocks.TexturedTerrainBlock;
import paulevs.edenring.blocks.MossyStoneBlock;
import ru.bclib.blocks.BaseLeavesBlock;
import ru.bclib.complexmaterials.ComplexMaterial;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.config.PathConfig;
import ru.bclib.registry.BlockRegistry;

public class EdenBlocks {
	public static final BlockRegistry REGISTRY = new BlockRegistry(EdenRing.EDEN_TAB, new PathConfig(EdenRing.MOD_ID, "blocks"));
	
	public static final Block EDEN_GRASS_BLOCK = register("eden_grass", new EdenGrassBlock());
	public static final Block GOLDEN_GRASS_BLOCK = register("golden_grass", new TexturedTerrainBlock());
	public static final Block EDEN_MYCELIUM = register("eden_mycelium", new TexturedTerrainBlock());
	public static final Block MOSSY_STONE = register("mossy_stone", new MossyStoneBlock());
	
	public static final Block AURITIS_LEAVES = register("auritis_leaves", new BaseLeavesBlock(Blocks.OAK_SAPLING, MaterialColor.GOLD));
	public static final ComplexMaterial AURITIS_MATERIAL = new WoodenComplexMaterial(EdenRing.MOD_ID, "auritis", "eden", MaterialColor.COLOR_BROWN, MaterialColor.GOLD).init(REGISTRY, EdenItems.REGISTRY, new PathConfig(EdenRing.MOD_ID, "recipes"));
	
	public static final Block BALOON_MUSHROOM_SMALL = register("baloon_mushroom_small", new BaloonMushroomSmallBlock());
	public static final Block BALOON_MUSHROOM_BLOCK = register("baloon_mushroom_block", new BaloonMushroomBlock());
	public static final Block BALOON_MUSHROOM_STEM = register("baloon_mushroom_stem", new BaloonMushroomStemBlock());
	public static final ComplexMaterial BALOON_MUSHROOM_MATERIAL = new WoodenComplexMaterial(EdenRing.MOD_ID, "baloon_mushroom", "eden", MaterialColor.COLOR_PURPLE, MaterialColor.COLOR_PURPLE).init(REGISTRY, EdenItems.REGISTRY, new PathConfig(EdenRing.MOD_ID, "recipes"));
	
	public static final Block MYCOTIC_GRASS = register("mycotic_grass", new SimplePlantBlock(true));
	
	public static void init() {}
	
	private static Block register(String name, Block block) {
		return REGISTRY.register(EdenRing.makeID(name), block);
	}
}
