package paulevs.edenring.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.blocks.EdenGrassBlock;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class StonePillar extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		center = getPosOnSurface(level, center);
		
		if (center.getY() < 5) {
			return false;
		}
		
		float height = MHelper.randRange(1.5F, 3.5F, random);
		center = center.above(Mth.floor(height));
		
		float r1 = MHelper.randRange(2.5F, 3.5F, random);
		float r2 = MHelper.randRange(1.5F, 2.5F, random);
		SDF sdf = new SDFCappedCone().setHeight(height * 2).setRadius1(r1).setRadius2(r2).setBlock(Blocks.STONE);
		int count = random.nextInt(3);
		float radius = r2 * MHelper.randRange(1.5F, 2F, random);
		for (int i = 0; i < count; i++) {
			height += radius * 0.7F;
			SDF sphere = new SDFSphere().setRadius(MHelper.randRange(2F, 4F, random)).setBlock(Blocks.STONE);
			sphere = new SDFScale3D().setScale(MHelper.randRange(2F, 3F, random), 1, MHelper.randRange(2F, 3F, random)).setSource(sphere);
			sphere = new SDFTranslate().setTranslate(0, height, 0).setSource(sphere);
			sdf = new SDFSmoothUnion().setRadius(4f).setSourceA(sdf).setSourceB(sphere);
			height += radius;
			radius += MHelper.randRange(0.75F, 1.5F, random);
		}
		
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		sdf = new SDFDisplacement().setFunction(pos -> {
			float disp = (float) noise.eval(pos.x() * 0.06F, pos.y() * 0.06F, pos.z() * 0.06F) * 2.0F;
			disp += (float) noise.eval(pos.x() * 0.1F, pos.y() * 0.1F, pos.z() * 0.1F) * 0.35F;
			return disp;
		}).setSource(sdf);
		
		sdf.addPostProcess(info -> {
			if (info.getStateUp().isAir() && noise.eval(info.getPos().getX() * 0.4F, info.getPos().getY() * 0.4F, info.getPos().getZ() * 0.4F) > 0.1) {
				if (random.nextInt(5) == 0) {
					info.setBlockPos(info.getPos().above(), Blocks.GRASS.defaultBlockState());
				}
				return EdenBlocks.MOSSY_STONE.defaultBlockState();
			}
			else if (noise.eval(info.getPos().getX() * 0.4F, info.getPos().getY() * 0.4F + 50, info.getPos().getZ() * 0.4F) > 0.3) {
				return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
			}
			else if (noise.eval(info.getPos().getX() * 0.4F, info.getPos().getY() * 0.4F + 100, info.getPos().getZ() * 0.4F) > 0.3) {
				return Blocks.COBBLESTONE.defaultBlockState();
			}
			else if (noise.eval(info.getPos().getX() * 0.4F, info.getPos().getY() * 0.4F + 150, info.getPos().getZ() * 0.4F) > 0.3) {
				if (info.getStateUp().isAir() && random.nextInt(5) == 0) {
					info.setBlockPos(info.getPos().above(), Blocks.GRASS.defaultBlockState());
				}
				return Blocks.MOSS_BLOCK.defaultBlockState();
			}
			
			return info.getState();
		});
		
		sdf.setReplaceFunction(state -> {
			return state.isAir() || state.getBlock() instanceof EdenGrassBlock || state.is(Blocks.DIRT);
		}).fillRecursive(level, center);
		
		return true;
	}
}
