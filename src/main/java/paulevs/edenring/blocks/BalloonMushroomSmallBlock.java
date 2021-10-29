package paulevs.edenring.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BalloonMushroomSmallBlock extends CustomSaplingBlock {
	public BalloonMushroomSmallBlock() {
		super(null);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {}
}
