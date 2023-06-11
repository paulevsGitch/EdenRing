package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdenGrassBlock extends GrassBlock implements BlockModelProvider, CustomColorProvider, RenderLayerProvider {
	public EdenGrassBlock() {
		super(FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool == null || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
			return Collections.singletonList(new ItemStack(Blocks.DIRT));
		}
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		return this.getBlockModel(blockId, this.defaultBlockState());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String modId = blockId.getNamespace();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", "minecraft:block/grass_block_top");
		textures.put("%side%", modId + ":block/grass_side");
		textures.put("%overlay%", modId + ":block/grass_side_overlay");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_GRASS_BLOCK, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		this.registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createRandomTopModel(modelId);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockColor getProvider() {
		return (blockState, blockAndTintGetter, blockPos, i) -> {
			return blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos) : GrassColor.get(0.5D, 1.0D);
		};
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemColor getItemProvider() {
		return (itemStack, i) -> GrassColor.get(0.5D, 1.0D);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
