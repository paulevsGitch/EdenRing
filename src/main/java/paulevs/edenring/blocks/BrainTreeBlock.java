package paulevs.edenring.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.EdenRing;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.BlockModelProvider;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.MHelper;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BrainTreeBlock extends BaseBlock implements BlockModelProvider, RenderLayerProvider {
	public static final BooleanProperty	ACTIVE = BlockProperties.ACTIVE;
	
	public BrainTreeBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).color(color).lightLevel(state -> state.getValue(ACTIVE) ? 15 : 0).randomTicks());
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (state.getValue(ACTIVE)) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
		}
		else if (random.nextInt(4) == 0) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			Vec3i[] offsets = MHelper.getOffsets(random);
			MutableBlockPos p = new MutableBlockPos();
			for (Vec3i offset: offsets) {
				p.set(pos).move(offset);
				BlockState sideBlock = world.getBlockState(p);
				if (sideBlock.getBlock() instanceof BrainTreeBlock && sideBlock.getValue(ACTIVE)) {
					world.setBlockAndUpdate(p, sideBlock.setValue(ACTIVE, false));
				}
			}
		}
	}
	
	/*@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		if (blockState.getValue(ACTIVE)) {
			String modId = stateId.getNamespace();
			String name = stateId.getPath();
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%texture%", modId + ":block/" + name);
			textures.put("%outline%", modId + ":block/lightning");
			Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/cube_with_outline.json"), textures);
			return ModelsHelper.fromPattern(pattern);
		}
		else {
			Optional<String> pattern = PatternsHelper.createBlockSimple(stateId);
			return ModelsHelper.fromPattern(pattern);
		}
	}*/
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Optional<String> pattern = PatternsHelper.createBlockSimple(blockState.getValue(ACTIVE) ? EdenRing.makeID(stateId.getPath() + "_active") : stateId);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
