package paulevs.edenring.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class GraviliteCrystalFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		center = new BlockPos(center.getX(), MHelper.randRange(64, 192, random), center.getZ());
		
		if (!level.getBlockState(center).isAir() || !level.getBlockState(center.above(10)).isAir() || !level.getBlockState(center.below(10)).isAir()) {
			return false;
		}
		
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			if (!level.getBlockState(center.relative(dir, 10)).isAir()) {
				return false;
			}
		}
		
		BlockState crystalBottom = EdenBlocks.GRAVILITE_SHARDS.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.DOWN);
		BlockState crystalTop = EdenBlocks.GRAVILITE_SHARDS.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP);
		BlockState pillar = EdenBlocks.GRAVILITE.defaultBlockState();
		
		int height = MHelper.randRange(8, 12, random);
		makeCluster(level, center, height, random, pillar, crystalTop, crystalBottom);
		
		int satellites = random.nextInt(5);
		float angleDelta = (float) Math.PI * 2 / satellites;
		float angle = (float) Math.PI * 2 * random.nextFloat();
		
		for (int i = 0; i < satellites; i++) {
			float distance = height * MHelper.randRange(0.75F, 1.25F, random);
			int dx = Mth.floor((float) Math.sin(angle) * distance + 0.5F);
			int dz = Mth.floor((float) Math.cos(angle) * distance + 0.5F);
			int h = Mth.floor(MHelper.randRange(height * 0.3F, height * 0.7F, random));
			if (h < 2) {
				continue;
			}
			makeCluster(level, center.offset(dx, 0, dz), h, random, pillar, crystalTop, crystalBottom);
			angle += angleDelta;
		}
		
		return true;
	}
	
	private void makeCluster(WorldGenLevel level, BlockPos center, int height, Random random, BlockState pillar, BlockState crystalTop, BlockState crystalBottom) {
		float radius = height * 0.2F;
		MutableBlockPos pos = new MutableBlockPos();
		int count = (int) MHelper.randRange(radius * 5, radius * 10, random);
		
		for (int i = 0; i < count; i++) {
			int px = Mth.floor(Mth.clamp((float) random.nextGaussian() * radius * 0.3F, -radius, radius) + 0.5F);
			int pz = Mth.floor(Mth.clamp((float) random.nextGaussian() * radius * 0.3F, -radius, radius) + 0.5F);
			pos.setX(px + center.getX());
			pos.setZ(pz + center.getZ());
			int h = Mth.floor(height - MHelper.length(px, pz) * 3 - random.nextInt(2));
			int minY = center.getY() - h;
			int maxY = center.getY() + h;
			
			pos.setY(center.getY());
			if (!level.getBlockState(pos).isAir()) {
				continue;
			}
			
			for (int py = minY; py <= maxY; py++) {
				pos.setY(py);
				if (level.getBlockState(pos).isAir()) {
					BlocksHelper.setWithoutUpdate(level, pos, pillar);
				}
			}
			
			pos.setY(minY - 1);
			if (level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).equals(pillar)) {
				BlocksHelper.setWithoutUpdate(level, pos, crystalBottom);
			}
			
			pos.setY(maxY + 1);
			if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).equals(pillar)) {
				BlocksHelper.setWithoutUpdate(level, pos, crystalTop);
			}
		}
	}
}
