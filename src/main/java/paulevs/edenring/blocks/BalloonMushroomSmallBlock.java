package paulevs.edenring.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.structures.EdenFeatures;

import java.util.Random;

public class BalloonMushroomSmallBlock extends CustomSaplingBlock {
	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);
	
	public BalloonMushroomSmallBlock() {
		super(() -> EdenFeatures.BALLOON_MUSHROOM_TREE.getFeature());
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return blockState.is(EdenBlocks.EDEN_MYCELIUM);
	}
}
