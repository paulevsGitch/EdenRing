package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;

public class GravityCompressorBlock extends BaseBlock {
	public static final BooleanProperty ACTIVE = BlockProperties.ACTIVE;
	
	public GravityCompressorBlock() {
		super(FabricBlockSettings.copyOf(Blocks.PISTON).luminance(state -> state.getValue(ACTIVE) ? 10 : 0));
		registerDefaultState(getStateDefinition().any().setValue(ACTIVE, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		boolean active = blockState.getValue(ACTIVE);
		String modId = stateId.getNamespace();
		String side = active ? stateId.getPath() + "_side_on" : stateId.getPath() + "_side_off";
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", "minecraft:block/piston_top");
		textures.put("%side%", modId + ":block/" + side);
		textures.put("%bottom%", modId + ":block/" + stateId.getPath() + "_bottom");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		String modId = blockId.getNamespace();
		String name = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", "minecraft:block/piston_top");
		textures.put("%side%", modId + ":block/" + name + "_side_off");
		textures.put("%bottom%", modId + ":block/" + name + "_bottom");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl) {
		if (level.hasNeighborSignal(pos)) {
			if (!level.getBlockState(pos).getValue(ACTIVE)) {
				level.setBlockAndUpdate(pos, defaultBlockState().setValue(ACTIVE, true));
			}
		}
		else {
			if (level.getBlockState(pos).getValue(ACTIVE)) {
				level.setBlockAndUpdate(pos, defaultBlockState());
			}
		}
	}
}
