package paulevs.edenring.world.features.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.registries.EdenBlocks;

public class SmallIslandFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		RandomSource random = featurePlaceContext.random();
		
		if (getYOnSurface(level, center.getX(), center.getZ()) > 0) {
			return false;
		}
		
		int size = MHelper.randRange(5, 9, random);
		
		MutableBlockPos pos = center.mutable();
		if (center.getY() == 0) {
			pos.setY(MHelper.randRange(64, 192, random));
		}
		makeCircle(level, pos, EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState(), size - 1, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle(level, pos, EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState(), size, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle(level, pos, Blocks.DIRT.defaultBlockState(), size - 1, random);
		
		pos.setY(pos.getY() - 1);
		makeCircle(level, pos, Blocks.STONE.defaultBlockState(), size - 2, random);
		
		if (size > 5) {
			pos.setY(pos.getY() - 1);
			makeCircle(level, pos, Blocks.STONE.defaultBlockState(), size - 4, random);
		}
		
		return true;
	}
	
	private void makeCircle(WorldGenLevel level, BlockPos pos, BlockState state, int radius, RandomSource random) {
		MutableBlockPos mut = pos.mutable();
		int r2 = radius * radius;
		for (int x = -radius; x <= radius; x++) {
			int x2 = x * x;
			mut.setX(pos.getX() + x);
			for (int z = -radius; z <= radius; z++) {
				int z2 = z * z;
				mut.setZ(pos.getZ() + z);
				BlockState worldState = level.getBlockState(mut);
				if (x2 + z2 <= r2 - random.nextInt(radius)) {
					if (worldState.isAir() || worldState.canBeReplaced()) {
						BlockState setState = state;
						if (state.is(EdenBlocks.EDEN_GRASS_BLOCK) && !level.getBlockState(mut.above()).isAir()) {
							setState = Blocks.DIRT.defaultBlockState();
						}
						BlocksHelper.setWithoutUpdate(level, mut, setState);
					}
				}
			}
		}
	}
}
