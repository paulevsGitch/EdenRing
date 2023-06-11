package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MossyStoneBlock extends BaseBlock {
	public MossyStoneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.STONE));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String modId = blockId.getNamespace();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", modId + ":block/mossy_stone_top");
		textures.put("%side%", modId + ":block/mossy_stone_side");
		textures.put("%bottom%", "minecraft:block/stone");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool == null || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
			return Collections.singletonList(new ItemStack(Blocks.STONE));
		}
		return Collections.singletonList(new ItemStack(this));
	}
}
