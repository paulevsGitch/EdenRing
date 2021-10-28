package paulevs.edenring.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class DepthScatterFeature extends DefaultFeature {
	private Block block;
	private Block[] walls;
	
	public DepthScatterFeature(Block block, Block... walls) {
		this.block = block;
		this.walls = walls;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		Random random = featurePlaceContext.random();
		BlockPos center = featurePlaceContext.origin();
		WorldGenLevel level = featurePlaceContext.level();
		
		int posX = center.getX() & 0xFFFFFFF0;
		int posZ = center.getZ() & 0xFFFFFFF0;
		
		MutableBlockPos pos = new MutableBlockPos();
		
		int iterations = MHelper.randRange(10, 50, random);
		for (int n = 0; n < iterations; n++) {
			int wx = posX | random.nextInt(16);
			int wz = posZ | random.nextInt(16);
			int wy = random.nextInt(256);
			int count = MHelper.randRange(30, 100, random);
			for (int i = 0; i < count; i++) {
				int px = wx + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
				int py = wy + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
				int pz = wz + Mth.floor(Mth.clamp(random.nextGaussian() * 2, -8, 8));
				pos.set(px, py, pz);
				
				BlockState state = level.getBlockState(pos);
				for (Block b: walls) {
					if (state.is(b)) {
						BlocksHelper.setWithoutUpdate(level, pos, block);
						break;
					}
				}
			}
		}
		
		return true;
	}
}
