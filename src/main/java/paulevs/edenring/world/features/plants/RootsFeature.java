package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import paulevs.edenring.world.features.basic.CeilScatterFeature;
import ru.bclib.util.BlocksHelper;

import java.util.Random;

public class RootsFeature extends CeilScatterFeature {
	@Override
	protected void generate(WorldGenLevel level, MutableBlockPos pos, Random random) {
		BlocksHelper.setWithoutUpdate(level, pos, Blocks.HANGING_ROOTS);
	}
}
