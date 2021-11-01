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
	private Block[] scatterIn;
	private int minCount;
	private int maxCount;
	private int minRadius;
	private int maxRadius;
	
	public DepthScatterFeature(Block block, int count, int radius, Block... scatterIn) {
		this.block = block;
		this.scatterIn = scatterIn;
		this.minCount = count / 3;
		this.maxCount = count;
		this.minRadius = radius / 2;
		this.maxRadius = radius;
	}
	
	public DepthScatterFeature(Block block, Block... scatterIn) {
		this(block, 50, 8, scatterIn);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		Random random = featurePlaceContext.random();
		BlockPos center = featurePlaceContext.origin();
		WorldGenLevel level = featurePlaceContext.level();
		
		int posX = center.getX() & 0xFFFFFFF0;
		int posZ = center.getZ() & 0xFFFFFFF0;
		
		MutableBlockPos pos = new MutableBlockPos();
		
		int iterations = MHelper.randRange(minCount, maxCount, random);
		for (int n = 0; n < iterations; n++) {
			int wx = posX | random.nextInt(16);
			int wz = posZ | random.nextInt(16);
			int wy = random.nextInt(256);
			int radius = MHelper.randRange(minRadius, maxRadius, random);
			int count = (int) (radius * radius * MHelper.randRange(0.7F, 2F, random));
			float multiplier = radius / 3.0F;
			for (int i = 0; i < count; i++) {
				int px = wx + Mth.floor(Mth.clamp(random.nextGaussian() * multiplier, -radius, radius));
				int py = wy + Mth.floor(Mth.clamp(random.nextGaussian() * multiplier, -radius, radius));
				int pz = wz + Mth.floor(Mth.clamp(random.nextGaussian() * multiplier, -radius, radius));
				pos.set(px, py, pz);
				
				BlockState state = level.getBlockState(pos);
				for (Block b: scatterIn) {
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
