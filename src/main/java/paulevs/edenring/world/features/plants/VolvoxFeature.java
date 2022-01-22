package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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
		float radius = MHelper.randRange(5F, 8F, random);
		BlockState volvox = EdenBlocks.VOLVOX_BLOCK.defaultBlockState();
		BlockState water = Blocks.WATER.defaultBlockState();
		makeSphere(level, pos, radius, random.nextFloat(), volvox, water);
	}
	
	private void makeSphere(WorldGenLevel level, BlockPos center, float radius, float fill, BlockState state, BlockState water) {
		double r21 = (double) radius * (double) radius;
		MutableBlockPos pos = new MutableBlockPos();
		double r22 = (double) (radius - 1) * (double) (radius - 1);
		int min = Mth.floor(-radius);
		int max = Mth.floor(radius + 1);
		int waterY = (int) Mth.lerp(fill, min, max);
		for (int x = min; x <= max; x++) {
			int sqrX = x * x;
			pos.setX(center.getX() + x);
			for (int y = min; y <= max; y++) {
				int sqrY = y * y;
				pos.setY(center.getY() + y);
				for (int z = min; z <= max; z++) {
					int sqrZ = z * z;
					int d = sqrX + sqrY + sqrZ;
					if (d <= r21) {
						pos.setZ(center.getZ() + z);
						if (d >= r22) {
							if (level.getBlockState(pos).getMaterial().isReplaceable()) {
								BlocksHelper.setWithoutUpdate(level, pos, state);
							}
						}
						else if (y < waterY) {
							BlocksHelper.setWithoutUpdate(level, pos, water);
						}
					}
				}
			}
		}
	}
}
