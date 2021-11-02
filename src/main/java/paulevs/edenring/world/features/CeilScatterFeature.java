package paulevs.edenring.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public abstract class CeilScatterFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		MutableBlockPos pos = center.mutable();
		int maxY = getYOnSurfaceWG(level, center.getX(), center.getZ());
		for (int y = maxY - 1; y > 5; y--) {
			pos.setY(y);
			if (level.getBlockState(pos).isAir()) {
				pos.setY(y + 1);
				if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.DOWN)) {
					int count = MHelper.randRange(5, 20, random);
					for (int n = 0; n < count; n++) {
						int px = center.getX() + Mth.floor(Mth.clamp(random.nextGaussian() * 2 + 0.5F, -8, 8));
						int pz = center.getZ() + Mth.floor(Mth.clamp(random.nextGaussian() * 2 + 0.5F, -8, 8));
						
						for (int i = 5; i > -5; i--) {
							pos.set(px, y + i, pz);
							if (level.getBlockState(pos).isAir()) {
								pos.setY(pos.getY() + 1);
								if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.DOWN)) {
									pos.setY(pos.getY() - 1);
									generate(level, pos, random);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	protected abstract void generate(WorldGenLevel level, MutableBlockPos pos, Random random);
}
