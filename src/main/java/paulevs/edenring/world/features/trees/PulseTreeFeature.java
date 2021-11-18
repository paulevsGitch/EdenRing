package paulevs.edenring.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.blocks.EdenBlockProperties.PulseTreeState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class PulseTreeFeature extends DefaultFeature {
	@Override
	@SuppressWarnings("deprecation")
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		if (!EdenBlocks.PULSE_TREE_SAPLING.canSurvive(EdenBlocks.PULSE_TREE_SAPLING.defaultBlockState(), level, center)) {
			return false;
		}
		
		MutableBlockPos pos = center.mutable();
		int h = MHelper.randRange(5, 12, random);
		for (int i = 1; i < h; i++) {
			pos.setY(center.getY() + i);
			if (!level.getBlockState(pos).isAir()) {
				h = i - 1;
				break;
			}
		}
		
		if (h < 5) {
			return false;
		}
		
		BlockState headMedium = EdenBlocks.PULSE_TREE.defaultBlockState().setValue(EdenBlockProperties.PULSE_TREE, PulseTreeState.HEAD_MEDIUM);
		BlockState headSmall = EdenBlocks.PULSE_TREE.defaultBlockState().setValue(EdenBlockProperties.PULSE_TREE, PulseTreeState.HEAD_SMALL);
		BlockState headBig = EdenBlocks.PULSE_TREE.defaultBlockState().setValue(EdenBlockProperties.PULSE_TREE, PulseTreeState.HEAD_BIG);
		BlockState stem = EdenBlocks.PULSE_TREE.defaultBlockState();
		int mediumCount = Mth.floor(h * 0.2F + 0.5F);
		int offsetCount = MHelper.randRange(2, 3, random);
		int bigCount = h - offsetCount - mediumCount * 2 - 1;
		
		pos.setY(center.getY());
		for (int i = 0; i < offsetCount; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, stem);
			pos.setY(pos.getY() + 1);
		}
		
		for (int i = 0; i < mediumCount; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, headMedium);
			pos.setY(pos.getY() + 1);
		}
		
		for (int i = 0; i < bigCount; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, headBig);
			pos.setY(pos.getY() + 1);
		}
		
		for (int i = 0; i < mediumCount; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, headMedium);
			pos.setY(pos.getY() + 1);
		}
		
		BlocksHelper.setWithoutUpdate(level, pos, headSmall);
		
		return true;
	}
}
