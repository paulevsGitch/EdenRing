package paulevs.edenring.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.blocks.entities.BrainTreeBlockEntity;
import ru.bclib.blocks.BaseBlockWithEntity;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.interfaces.BlockModelProvider;

public class BrainTreeBlock extends BaseBlockWithEntity implements BlockModelProvider {
	public static final BooleanProperty	ACTIVE = BlockProperties.ACTIVE;
	
	public BrainTreeBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).color(color).lightLevel(state -> state.getValue(ACTIVE) ? 9 : 0));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BrainTreeBlockEntity(blockPos, blockState);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation blockId) {
		return this.getBlockModel(blockId, this.defaultBlockState());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return BrainTreeBlockEntity::clientTick;
	}
}
