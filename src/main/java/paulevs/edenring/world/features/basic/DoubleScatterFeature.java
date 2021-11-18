package paulevs.edenring.world.features.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.util.BlocksHelper;

public class DoubleScatterFeature extends ScatterFeature {
	public DoubleScatterFeature(Block block) {
		super(block);
	}
	
	@Override
	protected void placeBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
		BlockPos above = pos.above();
		if (level.getBlockState(above).isAir()) {
			BlocksHelper.setWithoutUpdate(level, pos, state);
			BlocksHelper.setWithoutUpdate(level, above, state.setValue(BaseDoublePlantBlock.TOP, true));
		}
	}
}
