package paulevs.edenring;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import paulevs.edenring.blocks.OverlayPlantBlock;

public class LimphiumBlock extends OverlayPlantBlock {
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	
	public LimphiumBlock() {
		super(false);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(HALF);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Half half = state.getValue(HALF);
		if (half == Half.TOP) return world.getBlockState(pos.below()).is(this);
		BlockState below = world.getBlockState(pos.below());
		BlockState above = world.getBlockState(pos.above());
		return above.is(this) && (below.is(this) || isTerrain(below));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
}
