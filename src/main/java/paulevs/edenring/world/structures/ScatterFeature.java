package paulevs.edenring.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class ScatterFeature extends DefaultFeature {
	private Block block;
	
	public ScatterFeature(Block block) {
		this.block = block;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		Random random = featurePlaceContext.random();
		BlockPos center = featurePlaceContext.origin();
		WorldGenLevel level = featurePlaceContext.level();
		
		MutableBlockPos pos = new MutableBlockPos();
		int count = MHelper.randRange(10, 20, random);
		for (int i = 0; i < count; i++) {
			int px = center.getX() + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
			int pz = center.getZ() + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
			pos.setX(px);
			pos.setZ(pz);
			for (int y = 5; y > -5; y--) {
				pos.setY(center.getY() + y);
				if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.UP)) {
					pos.setY(pos.getY() + 1);
					if (level.getBlockState(pos).isAir()) {
						BlocksHelper.setWithoutUpdate(level, pos, block);
						break;
					}
				}
			}
		}
		
		return true;
	}
}
