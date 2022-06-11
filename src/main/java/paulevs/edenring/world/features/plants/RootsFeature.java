package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import org.betterx.bclib.util.BlocksHelper;
import paulevs.edenring.world.features.basic.CeilScatterFeature;

public class RootsFeature extends CeilScatterFeature {
	@Override
	protected void generate(WorldGenLevel level, MutableBlockPos pos, RandomSource random) {
		BlocksHelper.setWithoutUpdate(level, pos, Blocks.HANGING_ROOTS);
	}
}
