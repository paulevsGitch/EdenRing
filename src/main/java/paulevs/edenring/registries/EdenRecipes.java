package paulevs.edenring.registries;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.bclib.recipes.BCLRecipeBuilder;
import org.betterx.worlds.together.tag.v3.CommonItemTags;
import paulevs.edenring.EdenRing;
import paulevs.edenring.config.Configs;

public class EdenRecipes {

	public static void init() {
		BCLRecipeBuilder.crafting(EdenRing.makeID("gravilite_block"), EdenBlocks.GRAVILITE_BLOCK).setShape("##", "##").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).build();
		BCLRecipeBuilder.crafting(EdenRing.makeID("gravilite_shards"), EdenBlocks.GRAVILITE_SHARDS).setList("#").addMaterial('#', EdenBlocks.GRAVILITE_BLOCK).setOutputCount(4).build();
		BCLRecipeBuilder.crafting(EdenRing.makeID("gravilite_lamp"), EdenBlocks.GRAVILITE_LAMP).setShape(" I ", "I#I", " I ").addMaterial('#', EdenBlocks.GRAVILITE_BLOCK).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		BCLRecipeBuilder.crafting(EdenRing.makeID("gravilite_lantern_tall"), EdenBlocks.GRAVILITE_LANTERN_TALL).setShape("I", "#", "I").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		BCLRecipeBuilder.crafting(EdenRing.makeID("gravilite_lantern"), EdenBlocks.GRAVILITE_LANTERN).setShape(" I ", "I#I", " I ").setOutputCount(2).addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', CommonItemTags.IRON_INGOTS).build();
		
		Block log = EdenBlocks.BALLOON_MUSHROOM_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG);
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("baloon_mushroom_block"), log)
			.setShape("##", "##")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_STEM, EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("balloon_mushroom_branch"), EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.setList("#")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_STEM)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("balloon_mushroom_stem"), EdenBlocks.BALLOON_MUSHROOM_STEM)
			.setList("#")
			.addMaterial('#', EdenBlocks.BALLOON_MUSHROOM_BRANCH)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("balloon_mushroom_stem_block"), EdenBlocks.BALLOON_MUSHROOM_STEM)
			.setOutputCount(8)
			.setShape("#", "#")
			.addMaterial('#', log)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("pulse_tree_block"), EdenBlocks.PULSE_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG))
			.setShape("##", "##")
			.addMaterial('#', EdenBlocks.PULSE_TREE)
			.build();
		
		BCLRecipeBuilder.crafting(EdenRing.makeID("yellow_dye"), Items.YELLOW_DYE).setList("#").addMaterial('#', EdenBlocks.GOLDEN_GRASS).build();
		BCLRecipeBuilder.crafting(EdenRing.makeID("magenta_dye"), Items.MAGENTA_DYE).setList("#").addMaterial('#', EdenBlocks.VIOLUM).build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("gravity_compressor"), EdenBlocks.GRAVITY_COMPRESSOR)
			.setShape("IPI", "###", "RCR")
			.addMaterial('#', EdenBlocks.GRAVILITE_BLOCK)
			.addMaterial('P', Blocks.PISTON)
			.addMaterial('I', CommonItemTags.IRON_INGOTS)
			.addMaterial('C', Items.COPPER_INGOT)
			.addMaterial('R', Items.REDSTONE)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("limphium_paper"), Items.PAPER)
			.setOutputCount(3)
			.setShape("###")
			.addMaterial('#', EdenItems.LIMPHIUM_LEAF_DRYED)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("limphium_painting"), EdenItems.LIMPHIUM_PAINTING)
			.setShape("SSS", "SLS", "SSS")
			.addMaterial('L', EdenItems.LIMPHIUM_LEAF)
			.addMaterial('S', Items.STICK)
			.build();
		
		log = EdenBlocks.BRAIN_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG);
		Block log2 = EdenBlocks.BRAIN_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_STRIPPED_LOG);
		Block log3 = EdenBlocks.BRAIN_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_STRIPPED_BARK);
		Block log4 = EdenBlocks.BRAIN_TREE_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_BARK);
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("copper_framed_brain_tree_log"), EdenBlocks.COPPER_FRAMED_BRAIN_TREE_LOG)
			.setOutputCount(9)
			.setShape("MTM", "MTM", "MTM")
			.addMaterial('M', EdenBlocks.BRAIN_TREE_BLOCK_COPPER)
			.addMaterial('T', log, log2, log3, log4)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("iron_framed_brain_tree_log"), EdenBlocks.IRON_FRAMED_BRAIN_TREE_LOG)
			.setOutputCount(9)
			.setShape("MTM", "MTM", "MTM")
			.addMaterial('M', EdenBlocks.BRAIN_TREE_BLOCK_IRON)
			.addMaterial('T', log, log2, log3, log4)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("gold_framed_brain_tree_log"), EdenBlocks.GOLD_FRAMED_BRAIN_TREE_LOG)
			.setOutputCount(9)
			.setShape("MTM", "MTM", "MTM")
			.addMaterial('M', EdenBlocks.BRAIN_TREE_BLOCK_GOLD)
			.addMaterial('T', log, log2, log3, log4)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("volvox_block_dense"), EdenBlocks.VOLVOX_BLOCK_DENSE)
			.setShape("BB", "BB")
			.addMaterial('B', EdenBlocks.VOLVOX_BLOCK)
			.build();
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("volvox_block"), EdenBlocks.VOLVOX_BLOCK)
			.setOutputCount(4)
			.setList("B")
			.addMaterial('B', EdenBlocks.VOLVOX_BLOCK_DENSE)
			.build();
		
		BCLRecipeBuilder.smelting(EdenRing.makeID("slime_ball"), Items.SLIME_BALL).setInput(EdenBlocks.VOLVOX_BLOCK).build();
		BCLRecipeBuilder.smelting(EdenRing.makeID("slime_ball_4"), Items.SLIME_BALL).setInput(EdenBlocks.VOLVOX_BLOCK_DENSE).setOutputCount(4).build();
		BCLRecipeBuilder.smelting(EdenRing.makeID("limphium_leaf"), EdenItems.LIMPHIUM_LEAF_DRYED).setInput(EdenItems.LIMPHIUM_LEAF).build();
		
		Block[] coloredBlocks = EdenBlocks.MYCOTIC_LANTERN_COLORED.values().toArray(new Block[16]);
		EdenBlocks.MYCOTIC_LANTERN_COLORED.forEach(((color, block) -> {
			BCLRecipeBuilder
					.crafting(EdenRing.makeID("mycotic_lantern_" + color.getName()), block)
				.setGroup("eden_mycotic_lantern")
				.setOutputCount(8)
				.setShape("###", "#D#", "###")
				.addMaterial('#', coloredBlocks)
				.addMaterial('D', DyeItem.byColor(color))
				.build();
		}));
		
		EdenBlocks.BALLOON_MUSHROOM_SPOROCARP_COLORED.values().toArray(coloredBlocks);
		EdenBlocks.BALLOON_MUSHROOM_SPOROCARP_COLORED.forEach((color, block) -> {
			BCLRecipeBuilder
					.crafting(EdenRing.makeID("balloon_mushroom_sporocarp_" + color.getName()), block)
				.setGroup("eden_balloon_mushroom_sporocarp")
				.setOutputCount(8)
				.setShape("###", "#D#", "###")
				.addMaterial('#', coloredBlocks)
				.addMaterial('D', DyeItem.byColor(color))
				.build();
		});
		
		BCLRecipeBuilder
				.crafting(EdenRing.makeID("eden_book"), EdenItems.GUIDE_BOOK)
			.setOutputCount(2)
			.setList("BE")
			.addMaterial('B', EdenItems.GUIDE_BOOK)
			.addMaterial('E', Items.BOOK)
			.build();
	}
}
