package paulevs.edenring.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.util.BlocksHelper;

import java.util.Random;

public class MycoticGrass extends SimplePlantBlock {
	public MycoticGrass() {
		super(true);
	}
	
	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		BlockPos above = pos.above();
		if (level.getBlockState(above).isAir()) {
			BlockState tall = EdenBlocks.TALL_MYCOTIC_GRASS.defaultBlockState();
			BlocksHelper.setWithoutUpdate(level, above, tall.setValue(BaseDoublePlantBlock.TOP, true));
			BlocksHelper.setWithUpdate(level, pos, tall.setValue(BaseDoublePlantBlock.TOP, false));
		}
	}
}
