package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class VolvoxFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		BlockPos pos = getRandom(center, random);
		
		generateMedium(level, pos, random);
		
		return true;
	}
	
	private BlockPos getRandom(BlockPos pos, Random random) {
		int x = (pos.getX() & 0xFFFFFFF0) | random.nextInt(16);
		int z = (pos.getZ() & 0xFFFFFFF0) | random.nextInt(16);
		return new BlockPos(x, MHelper.randRange(64, 192, random), z);
	}
	
	private void generateMedium(WorldGenLevel level, BlockPos pos, Random random) {
		int radius = MHelper.randRange(5, 8, random);
		makeSphere(level, pos, radius, EdenBlocks.VOLVOX_BLOCK.defaultBlockState());
	}
	
	private void makeSphere(WorldGenLevel level, BlockPos center, int radius, BlockState state) {
		int r21 = radius * radius;
		MutableBlockPos pos = new MutableBlockPos();
		int r22 = (radius - 1) * (radius - 1);
		for (int x = -radius; x <= radius; x++) {
			int x2 = x * x;
			pos.setX(center.getX() + x);
			for (int y = -radius; y <= radius; y++) {
				int y2 = y * y;
				pos.setY(center.getY() + y);
				for (int z = -radius; z <= radius; z++) {
					int z2 = z * z;
					int d = x2 + y2 + z2;
					if (d < r21 && d >= r22) {
						pos.setZ(center.getZ() + z);
						if (level.getBlockState(pos).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(level, pos, state);
						}
					}
				}
			}
		}
	}
}
