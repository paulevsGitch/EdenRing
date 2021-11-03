package paulevs.edenring.world.features;

import com.google.common.collect.Lists;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class AuritisTreeFeature extends DefaultFeature {
	private static final Direction[] DIRECTIONS = Direction.values();
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final List<Vector3f> SPLINE;
	private static final List<Vector3f> ROOT;
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		Block below = level.getBlockState(center.below()).getBlock();
		if (!(below instanceof GrassBlock) && below != Blocks.DIRT) {
			return false;
		}
		
		BlockState log = EdenBlocks.AURITIS_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG).defaultBlockState();
		BlockState middle = EdenBlocks.AURITIS_OUTER_LEAVES.defaultBlockState().setValue(BlockStateProperties.BOTTOM, false);
		BlockState bottom = EdenBlocks.AURITIS_OUTER_LEAVES.defaultBlockState();
		BlockState leavesBlock = EdenBlocks.AURITIS_LEAVES.defaultBlockState();
		BlockState[] leaves = new BlockState[8];
		for (int i = 1; i <= 7; i++) {
			leaves[i] = leavesBlock.setValue(BlockStateProperties.DISTANCE, i);
		}
		leaves[0] = leaves[1];
		
		MutableBlockPos pos = center.mutable();
		
		int count = MHelper.randRange(4, 6, random);
		for (int i = 0; i < count; i++) {
			pos.setX(center.getX() + MHelper.randRange(-1, 1, random));
			pos.setZ(center.getZ() + MHelper.randRange(-1, 1, random));
			int maxY = center.getY() + MHelper.randRange(1, 3, random);
			int minY = center.getY() - 1;
			for (int y = maxY; y >= minY; y--) {
				pos.setY(y);
				BlockState state = level.getBlockState(pos);
				if (state.isAir() || state.getMaterial().isReplaceable() || state.equals(log) || state.equals(leaves)) {
					BlocksHelper.setWithoutUpdate(level, pos, log);
				}
			}
		}
		
		pos.setX(center.getX());
		pos.setZ(center.getZ());
		int h2 = center.getY() + MHelper.randRange(7, 11, random);
		for (int py = center.getY(); py < h2; py++) {
			pos.setY(py);
			BlockState state = level.getBlockState(pos);
			if (state.isAir() || state.getMaterial().isReplaceable() || state.equals(log) || state.equals(leaves)) {
				BlocksHelper.setWithoutUpdate(level, pos, log);
			}
			else {
				return true;
			}
		}
		
		count = MHelper.randRange(4, 6, random);
		float offset = random.nextFloat() * (float) Math.PI * 2;
		for (int i = 0; i < count; i++) {
			float angle = i / (float) count * (float) Math.PI * 2 + offset + MHelper.randRange(-0.1F, 0.1F, random);
			float dist = MHelper.randRange(0.5F, 0.75F, random);
			float dx = (float) Math.sin(angle) * dist;
			float dz = (float) Math.cos(angle) * dist;
			int length = MHelper.randRange(3, 6, random);
			pos.setY(h2 - length + random.nextInt(3));
			lineUp(level, random, pos, dx, dz, length + 2, log, leaves, middle, bottom);
		}
		
		pos.setY(h2);
		makeCanopy(level, random, pos, leaves, middle, bottom);
		
		return true;
	}
	
	private void lineDown(WorldGenLevel level, Random rand, BlockPos pos, float dx, float dz, int length, BlockState log) {
		for (int i = 0; i < length; i++) {
			MutableBlockPos mPos = new MutableBlockPos();
			mPos.setX(Mth.floor(pos.getX() + dx * i + MHelper.randRange(-0.1F, 0.1F, rand)));
			mPos.setZ(Mth.floor(pos.getZ() + dz * i + MHelper.randRange(-0.1F, 0.1F, rand)));
			mPos.setY(pos.getY() - i);
			
			BlockState state = level.getBlockState(mPos);
			if (state.isAir() || state.getMaterial().isReplaceable() || state.equals(log)) {
				BlocksHelper.setWithUpdate(level, mPos, log);
			}
			else {
				return;
			}
		}
	}
	
	private void lineUp(WorldGenLevel level, Random rand, BlockPos pos, float dx, float dz, int length, BlockState log, BlockState[] leaves, BlockState middle, BlockState bottom) {
		int last = length - 1;
		MutableBlockPos mPos = new MutableBlockPos();
		for (int i = 0; i < length; i++) {
			mPos.setX(Mth.floor(pos.getX() + dx * i + MHelper.randRange(-0.1F, 0.1F, rand)));
			mPos.setZ(Mth.floor(pos.getZ() + dz * i + MHelper.randRange(-0.1F, 0.1F, rand)));
			mPos.setY(pos.getY() + i);
			BlockState state = level.getBlockState(mPos);
			if (state.isAir() || state.getMaterial().isReplaceable() || state.equals(log) || state.equals(leaves)) {
				BlocksHelper.setWithoutUpdate(level, mPos, log);
			}
			else {
				return;
			}
			if (i == last) {
				makeCanopy(level, rand, mPos, leaves, middle, bottom);
			}
		}
	}
	
	private void makeCanopy(WorldGenLevel level, Random rand, BlockPos pos, BlockState[] leaves, BlockState middle, BlockState bottom) {
		int radius = MHelper.randRange(3, 5, rand);
		makeCircle(level, rand, pos.below(2), pos, radius - 2, leaves, middle, bottom);
		makeCircle(level, rand, pos.below(), pos, radius - 1, leaves, middle, bottom);
		makeCircle(level, rand, pos, pos, radius, leaves, middle, bottom);
		makeCircle(level, rand, pos.above(), pos, radius - 1, leaves, middle, bottom);
		makeCircle(level, rand, pos.above(2), pos, radius - 2, leaves, middle, bottom);
	}
	
	private void makeCircle(WorldGenLevel level, Random random, BlockPos pos, BlockPos center, int radius, BlockState[] leaves, BlockState middle, BlockState bottom) {
		int r2 = radius * radius;
		MutableBlockPos mPos = new MutableBlockPos();
		mPos.setY(pos.getY());
		for (int i = -radius; i <= radius; i++) {
			int i2 = i * i;
			for (int j = -radius; j <= radius; j++) {
				int j2 = j * j;
				if (i2 + j2 + random.nextInt(2) <= r2) {
					mPos.setX(pos.getX() + i);
					mPos.setZ(pos.getZ() + j);
					if (level.getBlockState(mPos).isAir()) {
						int dist = mPos.distManhattan(center);
						if (dist > 7) {
							continue;
						}
						BlocksHelper.setWithoutUpdate(level, mPos, leaves[dist]);
						if (random.nextInt(5) == 0) {
							int max = MHelper.randRange(3, 7, random);
							for (int n = 0; n <= max; n++) {
								mPos.setY(mPos.getY() - 1);
								if (level.getBlockState(mPos).isAir()) {
									BlocksHelper.setWithoutUpdate(level, mPos, n == max ? bottom : middle);
								}
								else {
									mPos.setY(mPos.getY() + 1);
									BlockState state = level.getBlockState(mPos);
									if (state.isAir() || state.equals(middle)) {
										BlocksHelper.setWithoutUpdate(level, mPos, bottom);
									}
									break;
								}
							}
							mPos.setY(pos.getY());
						}
					}
				}
			}
		}
	}
	
	private void leavesBall(WorldGenLevel world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EdenBlocks.AURITIS_LEAVES.defaultBlockState().setValue(LeavesBlock.DISTANCE, 6));
		SDF sub = new SDFScale().setScale(5).setSource(sphere);
		sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> (float) noise.eval(
			vec.x() * 0.2,
			vec.y() * 0.2,
			vec.z() * 0.2
		) * 2F).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> MHelper.randRange(-1.5F, 1.5F, random)).setSource(sphere);
		
		BlockState bark = EdenBlocks.AURITIS_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_BARK).defaultBlockState();
		BlockState log = EdenBlocks.AURITIS_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG).defaultBlockState();
		BlockState outer_middle = EdenBlocks.AURITIS_OUTER_LEAVES.defaultBlockState().setValue(BlockStateProperties.BOTTOM, false);
		BlockState outer_bottom = EdenBlocks.AURITIS_OUTER_LEAVES.defaultBlockState();
		BlockState leaves = EdenBlocks.AURITIS_LEAVES.defaultBlockState();
		
		MutableBlockPos mut = new MutableBlockPos();
		for (Direction d1 : BlocksHelper.HORIZONTAL) {
			BlockPos p = mut.set(pos).move(Direction.UP).move(d1).immutable();
			BlocksHelper.setWithoutUpdate(world, p, bark);
			for (Direction d2 : BlocksHelper.HORIZONTAL) {
				mut.set(p).move(Direction.UP).move(d2);
				BlocksHelper.setWithoutUpdate(world, p, bark);
			}
		}
		
		List<BlockPos> support = Lists.newArrayList();
		sphere.addPostProcess((info) -> {
			if (random.nextInt(6) == 0 && info.getStateDown().isAir()) {
				BlockPos d = info.getPos().below();
				support.add(d);
			}
			if (random.nextInt(15) == 0) {
				for (Direction dir : Direction.values()) {
					BlockState state = info.getState(dir, 2);
					if (state.isAir()) {
						return info.getState();
					}
				}
				info.setState(bark);
			}
			
			if (info.getState().equals(log) || info.getState().equals(bark)) {
				for (int x = -6; x < 7; x++) {
					int ax = Math.abs(x);
					mut.setX(x + info.getPos().getX());
					for (int z = -6; z < 7; z++) {
						int az = Math.abs(z);
						mut.setZ(z + info.getPos().getZ());
						for (int y = -6; y < 7; y++) {
							int ay = Math.abs(y);
							int d = ax + ay + az;
							if (d < 7) {
								mut.setY(y + info.getPos().getY());
								BlockState state = info.getState(mut);
								if (state.getBlock() instanceof LeavesBlock) {
									int distance = state.getValue(LeavesBlock.DISTANCE);
									if (d < distance) {
										info.setState(mut, state.setValue(LeavesBlock.DISTANCE, d));
									}
								}
							}
						}
					}
				}
			}
			return info.getState();
		});
		sphere.fillRecursiveIgnore(world, pos, IGNORE);
		BlocksHelper.setWithoutUpdate(world, pos, bark);
		
		support.forEach((bpos) -> {
			BlockState state = world.getBlockState(bpos);
			if (state.isAir() || state.is(EdenBlocks.AURITIS_OUTER_LEAVES)) {
				int count = MHelper.randRange(1, 3, random);
				mut.set(bpos);
				if (world.getBlockState(mut.above()).equals(leaves)) {
					for (int i = 0; i < count; i++) {
						mut.setY(mut.getY() - 1);
						if (world.isEmptyBlock(mut.below())) {
							BlocksHelper.setWithoutUpdate(world, mut, outer_middle);
						}
						else {
							break;
						}
					}
					BlocksHelper.setWithoutUpdate(world, mut, outer_bottom);
				}
			}
		});
	}
	
	private void makeRoots(WorldGenLevel world, BlockPos pos, float radius, Random random) {
		int count = (int) (radius * 1.5F);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2;
			float scale = radius * MHelper.randRange(0.85F, 1.15F, random);
			
			List<Vector3f> branch = SplineHelper.copySpline(ROOT);
			SplineHelper.rotateSpline(branch, angle);
			SplineHelper.scale(branch, scale);
			Vector3f last = branch.get(branch.size() - 1);
			if (REPLACE.apply(world.getBlockState(pos.offset(last.x(), last.y(), last.z())))) {
				SplineHelper.fillSplineForce(branch, world, EdenBlocks.AURITIS_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_BARK).defaultBlockState(), pos, REPLACE);
			}
		}
	}
	
	static {
		REPLACE = state -> {
			if (state.is(BlockTags.LEAVES)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = state -> state.is(BlockTags.LOGS);
		
		SPLINE = Lists.newArrayList(
			new Vector3f(0.00F, 0.00F, 0.00F),
			new Vector3f(0.10F, 0.35F, 0.00F),
			new Vector3f(0.20F, 0.50F, 0.00F),
			new Vector3f(0.30F, 0.55F, 0.00F),
			new Vector3f(0.42F, 0.70F, 0.00F),
			new Vector3f(0.50F, 1.00F, 0.00F)
		);
		
		ROOT = Lists.newArrayList(
			new Vector3f(0.1F, 0.70F, 0),
			new Vector3f(0.3F, 0.30F, 0),
			new Vector3f(0.7F, 0.05F, 0),
			new Vector3f(0.8F, -0.20F, 0)
		);
		SplineHelper.offset(ROOT, new Vector3f(0, -0.45F, 0));
	}
}
