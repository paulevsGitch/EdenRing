package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.blocks.BaseVineBlock;
import org.betterx.bclib.blocks.BlockProperties.TripleShape;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;

public class SimpleVineBlock extends BaseVineBlock {
	private final boolean tripple;
	
	public SimpleVineBlock() {
		this(false);
	}
	
	public SimpleVineBlock(boolean tripple) {
		this.tripple = tripple;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String modId = stateId.getNamespace();
		String name = stateId.getPath();
		if (tripple && blockState.getValue(BaseVineBlock.SHAPE) == TripleShape.TOP) {
			name += "_top";
		}
		if (blockState.getValue(BaseVineBlock.SHAPE) == TripleShape.BOTTOM) {
			name += "_bottom";
		}
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%modid%", modId);
		textures.put("%texture%", name);
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%modid%", modId);
		textures.put("%texture%", name + "_bottom");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
