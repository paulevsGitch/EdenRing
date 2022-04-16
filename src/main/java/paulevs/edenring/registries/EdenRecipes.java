package paulevs.edenring.registries;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import paulevs.edenring.EdenRing;
import ru.bclib.api.tag.CommonItemTags;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.config.PathConfig;
import ru.bclib.recipes.FurnaceRecipe;
import ru.bclib.recipes.GridRecipe;

public class EdenRecipes {
	public static final PathConfig CONFIG = new PathConfig(EdenRing.MOD_ID, "recipes");
	
	public static void init() {
		GridRecipe.make(EdenRing.makeID("gravilite_block"), EdenBlocks.GRAVILITE_BLOCK).checkConfig(CONFIG).setShape("##", "##").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_shards"), EdenBlocks.GRAVILITE_SHARDS).checkConfig(CONFIG).setList("#").addMaterial('#', EdenBlocks.GRAVILITE_BLOCK).setOutputCount(4).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lamp"), EdenBlocks.GRAVILITE_LAMP).checkConfig(CONFIG).setShape(" I ", "I#I", " I ").addMaterial('#', EdenBlocks.GRAVILITE_BLOCK).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lantern_tall"), EdenBlocks.GRAVILITE_LANTERN_TALL).checkConfig(CONFIG).setShape("I", "#", "I").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lantern"), EdenBlocks.GRAVILITE_LANTERN).checkConfig(CONFIG).setShape(" I ", "I#I", " I ").setOutputCount(2).addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		
		Block log = EdenBlocks.BALLOON_MUSHROOM_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG);
		GridRecipe
			.make(EdenRing.makeID("baloon_mushroom_block"), log)
			.checkConfig(CONFIG)
			.setShape("##", "##")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_STEM, EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.build();
		
		GridRecipe
			.make(EdenRing.makeID("balloon_mushroom_branch"), EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.checkConfig(CONFIG)
			.setList("#")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_STEM)
			.build();
		
		GridRecipe
			.make(EdenRing.makeID("balloon_mushroom_stem"), EdenBlocks.BALLOON_MUSHROOM_STEM)
			.checkConfig(CONFIG)
			.setList("#")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.build();
		
		GridRecipe
			.make(EdenRing.makeID("balloon_mushroom_stem_block"), EdenBlocks.BALLOON_MUSHROOM_STEM)
			.setOutputCount(8)
			.checkConfig(CONFIG)
			.setShape("#", "#")
			.addMaterial('#', log)
			.build();
		
		GridRecipe
			.make(EdenRing.makeID("pulse_tree_block"), EdenBlocks.PULSE_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG))
			.checkConfig(CONFIG)
			.setShape("##", "##")
			.addMaterial('#', EdenBlocks.PULSE_TREE)
			.build();
		
		GridRecipe.make(EdenRing.makeID("yellow_dye"), Items.YELLOW_DYE).checkConfig(CONFIG).setList("#").addMaterial('#', EdenBlocks.GOLDEN_GRASS).build();
		GridRecipe.make(EdenRing.makeID("magenta_dye"), Items.MAGENTA_DYE).checkConfig(CONFIG).setList("#").addMaterial('#', EdenBlocks.VIOLUM).build();
		
		GridRecipe
			.make(EdenRing.makeID("gravity_compressor"), EdenBlocks.GRAVITY_COMPRESSOR)
			.checkConfig(CONFIG)
			.setShape("IPI", "###", "RCR")
			.addMaterial('#', EdenBlocks.GRAVILITE_BLOCK)
			.addMaterial('P', Blocks.PISTON)
			.addMaterial('I', CommonItemTags.IRON_INGOTS)
			.addMaterial('C', Items.COPPER_INGOT)
			.addMaterial('R', Items.REDSTONE)
			.build();
		
		GridRecipe
			.make(EdenRing.makeID("limphium_paper"), Items.PAPER)
			.checkConfig(CONFIG)
			.setOutputCount(3)
			.setShape("###")
			.addMaterial('#', EdenItems.LIMPHIUM_LEAF_DRYED)
			.build();
		
		FurnaceRecipe.make(EdenRing.MOD_ID, "iron_nugget", EdenBlocks.IRON_GRASS, Items.IRON_NUGGET).checkConfig(CONFIG).buildWithBlasting();
		FurnaceRecipe.make(EdenRing.MOD_ID, "gold_nugget", EdenBlocks.GOLD_GRASS, Items.GOLD_NUGGET).checkConfig(CONFIG).buildWithBlasting();
		FurnaceRecipe.make(EdenRing.MOD_ID, "slime_ball", EdenBlocks.VOLVOX_BLOCK, Items.SLIME_BALL).checkConfig(CONFIG).build();
		FurnaceRecipe.make(EdenRing.MOD_ID, "limphium_leaf", EdenItems.LIMPHIUM_LEAF, EdenItems.LIMPHIUM_LEAF_DRYED).checkConfig(CONFIG).build();
		
		Block[] coloredBlocks = EdenBlocks.MYCOTIC_LANTERN_COLORED.values().toArray(new Block[16]);
		EdenBlocks.MYCOTIC_LANTERN_COLORED.forEach(((color, block) -> {
			GridRecipe
				.make(EdenRing.makeID("mycotic_lantern_" + color.getName()), block)
				.setGroup("eden_mycotic_lantern")
				.checkConfig(CONFIG)
				.setOutputCount(8)
				.setShape("###", "#D#", "###")
				.addMaterial('#', coloredBlocks)
				.addMaterial('D', DyeItem.byColor(color))
				.build();
		}));
		
		EdenBlocks.BALLOON_MUSHROOM_SPOROCARP_COLORED.values().toArray(coloredBlocks);
		EdenBlocks.BALLOON_MUSHROOM_SPOROCARP_COLORED.forEach((color, block) -> {
			GridRecipe
				.make(EdenRing.makeID("balloon_mushroom_sporocarp_" + color.getName()), block)
				.setGroup("eden_balloon_mushroom_sporocarp")
				.checkConfig(CONFIG)
				.setOutputCount(8)
				.setShape("###", "#D#", "###")
				.addMaterial('#', coloredBlocks)
				.addMaterial('D', DyeItem.byColor(color))
				.build();
		});
		
		CONFIG.saveChanges();
	}
}
