package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.EdenRing;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.interfaces.CustomColorProvider;
import ru.bclib.util.ColorUtil;

import java.util.Map;
import java.util.Optional;

public class OverlayDoublePlantBlock extends BaseDoublePlantBlock implements CustomColorProvider {
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String modId = stateId.getNamespace();
		String name = stateId.getPath() + (blockState.getValue(TOP) ? "_top" : "_bottom");
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", modId + ":block/" + name + "_color");
		textures.put("%overlay%", modId + ":block/" + name + "_overlay");
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/tinted_cross_overlay.json"), textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", modId + ":block/" + name + "_top_color");
		textures.put("%overlay%", modId + ":block/" + name + "_top_overlay");
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/item/tinted_overlay.json"), textures);
		return ModelsHelper.fromPattern(pattern);
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
		return (itemStack, i) -> i == 1 ? GrassColor.get(0.5D, 1.0D) : ColorUtil.color(255, 255, 255);
	}
	
	@Override
	protected boolean isTerrain(BlockState blockState) {
		Block block = blockState.getBlock();
		return block instanceof GrassBlock || block instanceof MossyStoneBlock || blockState.is(BlockTags.DIRT) || blockState.is(Blocks.FARMLAND);
	}
}
