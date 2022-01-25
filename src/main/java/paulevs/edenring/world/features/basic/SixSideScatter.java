package paulevs.edenring.world.features.basic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.blocks.SixSidePlant;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class SixSideScatter extends DefaultFeature {
	private SixSidePlant block;
	
	public SixSideScatter(SixSidePlant block) {
		this.block = block;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		Random random = featurePlaceContext.random();
		BlockPos center = featurePlaceContext.origin();
		WorldGenLevel level = featurePlaceContext.level();
		
		int minX = center.getX() & 0xFFFFFFF0;
		int minZ = center.getZ() & 0xFFFFFFF0;
		
		int px = minX | random.nextInt(16);
		int py = MHelper.randRange(32, 224, random);
		int pz = minZ | random.nextInt(16);
		
		MutableBlockPos pos = new MutableBlockPos();
		int count = MHelper.randRange(10, 20, random);
		for (int i = 0; i < count; i++) {
			pos.set(
				px + MHelper.randRange(-4, 4, random),
				py + MHelper.randRange(-4, 4, random),
				pz + MHelper.randRange(-4, 4, random)
			);
			if (level.getBlockState(pos).isAir()) {
				BlockState state = block.getAttachedState(level, pos);
				if (state != null) {
					BlocksHelper.setWithoutUpdate(level, pos, state);
				}
			}
		}
		
		return true;
	}
}
