package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.blocks.entities.EdenPortalBlockEntity;
import ru.bclib.blocks.BaseBlockWithEntity;

public class EdenPortalCenterBlock extends BaseBlockWithEntity {
	public EdenPortalCenterBlock() {
		super(FabricBlockSettings.copyOf(Blocks.BARRIER).luminance(15).noCollision().noOcclusion().noDrops());
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new EdenPortalBlockEntity(blockPos, blockState);
	}
	
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
		return level.isClientSide() ? EdenPortalBlockEntity::clientTick : EdenPortalBlockEntity::serverTick;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.INVISIBLE;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return 1.0F;
	}
}
