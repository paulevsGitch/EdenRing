package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.EdenRing;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;

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
}
