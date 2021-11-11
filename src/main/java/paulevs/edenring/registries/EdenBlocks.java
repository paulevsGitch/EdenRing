package paulevs.edenring.registries;


import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.AquatusBlock;
import paulevs.edenring.blocks.AquatusRootsBlock;
import paulevs.edenring.blocks.AquatusSapling;
import paulevs.edenring.blocks.AuritisLeavesBlock;
import paulevs.edenring.blocks.BalloonMushroomBlock;
import paulevs.edenring.blocks.BalloonMushroomSmallBlock;
import paulevs.edenring.blocks.BalloonMushroomStemBlock;
import paulevs.edenring.blocks.BrainTreeBlock;
import paulevs.edenring.blocks.BrainTreeLogBlock;
import paulevs.edenring.blocks.CustomSaplingBlock;
import paulevs.edenring.blocks.EdenGrassBlock;
import paulevs.edenring.blocks.EdenMossBlock;
import paulevs.edenring.blocks.EdenPortalBlock;
import paulevs.edenring.blocks.EdenPortalCenterBlock;
import paulevs.edenring.blocks.GraviliteBlock;
import paulevs.edenring.blocks.GraviliteLampBlock;
import paulevs.edenring.blocks.GraviliteLanternBlock;
import paulevs.edenring.blocks.GraviliteShardsBlock;
import paulevs.edenring.blocks.GraviliteTallLanternBlock;
import paulevs.edenring.blocks.MossyStoneBlock;
import paulevs.edenring.blocks.OverlayDoublePlantBlock;
import paulevs.edenring.blocks.OverlayPlantBlock;
import paulevs.edenring.blocks.OverlayVineBlock;
import paulevs.edenring.blocks.PulseTreeBlock;
import paulevs.edenring.blocks.SimplePlantBlock;
import paulevs.edenring.blocks.TexturedTerrainBlock;
import ru.bclib.api.BonemealAPI;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseLeavesBlock;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.complexmaterials.ComplexMaterial;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.config.PathConfig;
import ru.bclib.mixin.common.ComposterBlockAccessor;
import ru.bclib.registry.BlockRegistry;

public class EdenBlocks {
	public static final BlockRegistry REGISTRY = new BlockRegistry(EdenRing.EDEN_TAB, new PathConfig(EdenRing.MOD_ID, "blocks"));
	
	public static final Block EDEN_GRASS_BLOCK = register("eden_grass", new EdenGrassBlock());
	public static final Block EDEN_MYCELIUM = register("eden_mycelium", new TexturedTerrainBlock());
	public static final Block MOSSY_STONE = register("mossy_stone", new MossyStoneBlock());
	
	public static final Block AURITIS_SAPLING = register("auritis_sapling", new CustomSaplingBlock(() -> EdenFeatures.AURITIS_TREE.getFeature()));
	public static final Block AURITIS_LEAVES = register("auritis_leaves", new AuritisLeavesBlock());
	public static final ComplexMaterial AURITIS_MATERIAL = new WoodenComplexMaterial(EdenRing.MOD_ID, "auritis", "eden", MaterialColor.COLOR_BROWN, MaterialColor.GOLD).init(REGISTRY, EdenItems.REGISTRY, new PathConfig(EdenRing.MOD_ID, "recipes"));
	
	public static final Block BALLOON_MUSHROOM_SMALL = register("balloon_mushroom_small", new BalloonMushroomSmallBlock());
	public static final Block BALLOON_MUSHROOM_BLOCK = register("balloon_mushroom_block", new BalloonMushroomBlock());
	public static final Block BALLOON_MUSHROOM_STEM = register("balloon_mushroom_stem", new BalloonMushroomStemBlock());
	public static final ComplexMaterial BALLOON_MUSHROOM_MATERIAL = new WoodenComplexMaterial(EdenRing.MOD_ID, "balloon_mushroom", "eden", MaterialColor.COLOR_PURPLE, MaterialColor.COLOR_PURPLE).init(REGISTRY, EdenItems.REGISTRY, EdenRecipes.CONFIG);
	
	public static final Block PULSE_TREE_SAPLING = register("pulse_tree_sapling", new CustomSaplingBlock(() -> EdenFeatures.PULSE_TREE.getFeature()));
	public static final Block PULSE_TREE = register("pulse_tree", new PulseTreeBlock());
	public static final ComplexMaterial PULSE_TREE_MATERIAL = new WoodenComplexMaterial(EdenRing.MOD_ID, "pulse_tree", "eden", MaterialColor.COLOR_CYAN, MaterialColor.COLOR_CYAN).init(REGISTRY, EdenItems.REGISTRY, EdenRecipes.CONFIG);
	
	public static final Block BRAIN_TREE_LOG = register("brain_tree_log", new BrainTreeLogBlock());
	public static final Block BRAIN_TREE_BLOCK_IRON = register("brain_tree_block_iron", new BrainTreeBlock(MaterialColor.COLOR_LIGHT_GRAY));
	public static final Block BRAIN_TREE_BLOCK_COPPER = register("brain_tree_block_copper", new BrainTreeBlock(MaterialColor.COLOR_ORANGE));
	public static final Block BRAIN_TREE_BLOCK_GOLD = register("brain_tree_block_gold", new BrainTreeBlock(MaterialColor.GOLD));
	
	public static final Block AQUATUS_SAPLING = register("aquatus_sapling", new AquatusSapling());
	public static final Block AQUATUS_BLOCK = register("aquatus_block", new AquatusBlock());
	public static final Block AQUATUS_ROOTS = registerBO("aquatus_roots", new AquatusRootsBlock());
	
	public static final Block EDEN_MOSS = register("eden_moss", new EdenMossBlock());
	
	public static final Block MYCOTIC_GRASS = register("mycotic_grass", new SimplePlantBlock(true));
	public static final Block GOLDEN_GRASS = register("golden_grass", new OverlayPlantBlock(true));
	public static final Block IRON_GRASS = register("iron_grass", new OverlayPlantBlock(true));
	public static final Block COPPER_GRASS = register("copper_grass", new OverlayPlantBlock(true));
	public static final Block GOLD_GRASS = register("gold_grass", new OverlayPlantBlock(true));
	
	public static final Block VIOLUM = register("violum", new OverlayDoublePlantBlock());
	
	public static final Block EDEN_VINE = register("eden_vine", new OverlayVineBlock());
	
	public static final Block GRAVILITE_BLOCK = register("gravilite_block", new GraviliteBlock());
	public static final Block GRAVILITE_SHARDS = register("gravilite_shards", new GraviliteShardsBlock());
	public static final Block GRAVILITE_LAMP = register("gravilite_lamp", new GraviliteLampBlock());
	public static final Block GRAVILITE_LANTERN = register("gravilite_lantern", new GraviliteLanternBlock());
	public static final Block GRAVILITE_LANTERN_TALL = register("gravilite_lantern_tall", new GraviliteTallLanternBlock());
	
	public static final Block PORTAL_BLOCK = registerBO("portal_block", new EdenPortalBlock());
	public static final Block PORTAL_CENTER = registerBO("portal_center", new EdenPortalCenterBlock());
	
	public static void init() {
		//Registry.BLOCK.stream().filter(block -> Registry.BLOCK.getKey(block).getNamespace().equals(EdenRing.MOD_ID)).forEach(block -> {
		BlockRegistry.getModBlocks(EdenRing.MOD_ID).forEach(block -> {
			Properties properties = ((AbstractBlockAccessor) block).getSettings();
			Material material = ((AbstractBlockSettingsAccessor) properties).getMaterial();
			
			if (block instanceof BaseLeavesBlock) {
				TagAPI.addTag(BlockTags.MINEABLE_WITH_HOE, block);
				TagAPI.addTag(BlockTags.LEAVES, block);
				TagAPI.addTag(ItemTags.LEAVES, block);
				ComposterBlockAccessor.callAdd(0.3F, block);
			}
			else if (block instanceof GrassBlock) {
				TagAPI.addTag(BlockTags.MINEABLE_WITH_SHOVEL, block);
			}
			else if (material == Material.PLANT || material == Material.REPLACEABLE_PLANT) {
				TagAPI.addTag(BlockTags.MINEABLE_WITH_HOE, block);
				if (block.asItem() != Items.AIR) {
					ComposterBlockAccessor.callAdd(0.1F, block);
				}
			}
			else if (material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL || material == Material.AMETHYST) {
				TagAPI.addTag(BlockTags.MINEABLE_WITH_PICKAXE, block);
			}
			
			if (block instanceof BaseVineBlock) {
				TagAPI.addTag(BlockTags.CLIMBABLE, block);
			}
		});
		
		BonemealAPI.addLandGrass(EdenBiomes.GOLDEN_FOREST.getID(), GOLDEN_GRASS, EDEN_GRASS_BLOCK);
		BonemealAPI.addLandGrass(EdenBiomes.GOLDEN_FOREST.getID(), Blocks.GRASS, EDEN_GRASS_BLOCK);
		
		BonemealAPI.addSpreadableBlock(MOSSY_STONE, Blocks.STONE);
	}
	
	private static Block register(String name, Block block) {
		return REGISTRY.register(EdenRing.makeID(name), block);
	}
	
	private static Block registerBO(String name, Block block) {
		return REGISTRY.registerBlockOnly(EdenRing.makeID(name), block);
	}
	
	public static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return false;
	}
}
