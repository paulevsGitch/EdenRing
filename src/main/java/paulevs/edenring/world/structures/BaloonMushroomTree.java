package paulevs.edenring.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.blocks.EdenBlockProperties.BalloonMushroomStemState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class BaloonMushroomTree extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		MutableBlockPos pos = center.mutable();
		
		if (!(level.getBlockState(pos.below()).getBlock() instanceof GrassBlock)) {
			return false;
		}
		
		int h = MHelper.randRange(3, 12, random);
		for (int i = 1; i <= h; i++) {
			pos.setY(center.getY() + i);
			if (!level.getBlockState(pos).isAir()) {
				h = i - 1;
			}
		}
		if (h < 3) {
			return false;
		}
		
		BlockState stem = EdenBlocks.BALLOON_MUSHROOM_STEM.defaultBlockState();
		if (h > 5 && random.nextInt(8) == 0) {
			for (int i = 0; i < h; i++) {
				pos.setY(center.getY() + i);
				BlocksHelper.setWithoutUpdate(level, pos, stem);
			}
			
			BlockState head = EdenBlocks.BALLOON_MUSHROOM_BLOCK.defaultBlockState();
			for (int y = 0; y < 3; y++) {
				pos.setY(center.getY() + h + y);
				for (int x = -1; x < 2; x++) {
					pos.setX(center.getX() + x);
					for (int z = -1; z < 2; z++) {
						pos.setZ(center.getZ() + z);
						if (level.getBlockState(pos).isAir()) {
							BlocksHelper.setWithoutUpdate(level, pos, head);
						}
					}
				}
			}
		}
		else {
			BlockState thin = stem.setValue(EdenBlockProperties.BALLOON_MUSHROOM_STEM, BalloonMushroomStemState.THIN);
			BlockState thin_up = stem.setValue(EdenBlockProperties.BALLOON_MUSHROOM_STEM, BalloonMushroomStemState.THIN_TOP);
			int hMax = h - 1;
			for (int i = 0; i < h; i++) {
				pos.setY(center.getY() + i);
				BlocksHelper.setWithoutUpdate(level, pos, i == hMax ? thin_up : thin);
			}
			pos.setY(center.getY() + h);
			BlocksHelper.setWithoutUpdate(level, pos, EdenBlocks.BALLOON_MUSHROOM_BLOCK);
		}
		
		return true;
	}
}
