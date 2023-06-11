package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.betterx.bclib.blocks.BaseDoublePlantBlock;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.items.tool.BaseShearsItem;
import paulevs.edenring.EdenRing;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdenDoublePlantBlock extends BaseDoublePlantBlock {
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String modId = stateId.getNamespace();
		String name = stateId.getPath() + (blockState.getValue(TOP) ? "_top" : "_bottom");
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", name);
		textures.put("%modid%", modId);
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		return  ModelsHelper.createBlockItem(EdenRing.makeID(itemID.getPath() + "_top"));
	}
	
	@Override
	protected boolean isTerrain(BlockState blockState) {
		Block block = blockState.getBlock();
		return block instanceof GrassBlock || block instanceof MossyStoneBlock || blockState.is(BlockTags.DIRT) || blockState.is(Blocks.FARMLAND);
	}
	
	@Override
	// TODO remove that fix after BCLib fix it
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && BaseShearsItem.isShear(tool) || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Collections.EMPTY_LIST;
		}
	}
}
