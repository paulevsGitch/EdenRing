package paulevs.edenring.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class FloorScatterFeature extends DefaultFeature {
	private Block block;
	private Block floor;
	
	public FloorScatterFeature(Block block, Block floor) {
		this.block = block;
		this.floor = floor;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		Random random = featurePlaceContext.random();
		BlockPos center = featurePlaceContext.origin();
		WorldGenLevel level = featurePlaceContext.level();
		
		MutableBlockPos pos = new MutableBlockPos();
		int count = MHelper.randRange(100, 200, random);
		for (int i = 0; i < count; i++) {
			int px = center.getX() + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
			int py = center.getY() + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
			int pz = center.getZ() + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
			pos.set(px, py, pz);
			if (level.getBlockState(pos).is(floor)) {
				BlocksHelper.setWithoutUpdate(level, pos, block);
			}
		}
		
		return true;
	}
}
