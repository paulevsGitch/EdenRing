package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.features.basic.ScatterFeature;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

import java.util.Random;

public class TallMushroomFeature extends ScatterFeature {
	public TallMushroomFeature() {
		super(EdenBlocks.TALL_BALLOON_MUSHROOM);
	}
	
	@Override
	protected int getCount(Random random) {
		return MHelper.randRange(5, 10, random);
	}
	
	@Override
	protected void placeBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
		byte height = (byte) level.getRandom().nextInt(4);
		MutableBlockPos p = pos.mutable();
		for (byte i = 0; i <= height; i++) {
			state = state.setValue(EdenBlockProperties.TEXTURE_4, (int) i);
			if (i == height) state = state.setValue(EdenBlockProperties.TEXTURE_4, 3);
			if (level.getBlockState(p.above()).isAir()) {
				BlocksHelper.setWithoutUpdate(level, p, state);
				p.setY(p.getY() + 1);
			}
			else {
				state = state.setValue(EdenBlockProperties.TEXTURE_4, 3);
				BlocksHelper.setWithoutUpdate(level, p, state);
				return;
			}
		}
	}
}
