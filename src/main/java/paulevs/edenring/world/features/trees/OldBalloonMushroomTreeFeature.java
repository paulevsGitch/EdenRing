package paulevs.edenring.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.blocks.EdenBlockProperties.BalloonMushroomStemState;
import paulevs.edenring.blocks.EdenBlockProperties.QuadShape;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.complexmaterials.WoodenComplexMaterial;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OldBalloonMushroomTreeFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		Block below = level.getBlockState(center.below()).getBlock();
		if (!(below instanceof GrassBlock) && below != Blocks.DIRT) {
			return false;
		}
		
		BlockState log = EdenBlocks.BALLOON_MUSHROOM_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_LOG).defaultBlockState();
		
		MutableBlockPos p = center.mutable().setY(center.getY() + 4);
		if (!level.getBlockState(center).is(EdenBlocks.BALLOON_MUSHROOM_SMALL)) {
			for (int x = -14; x <= 14; x++) {
				p.setX(center.getX() + x);
				for (int z = -14; z <= 14; z++) {
					if (Math.abs(x) + Math.abs(z) < 20) {
						p.setZ(center.getZ() + z);
						if (level.getBlockState(p).is(log.getBlock())) {
							return false;
						}
					}
				}
			}
		}
		
		BlockState bark = EdenBlocks.BALLOON_MUSHROOM_MATERIAL.getBlock(WoodenComplexMaterial.BLOCK_BARK).defaultBlockState();
		BlockState block = EdenBlocks.BALLOON_MUSHROOM_BLOCK.defaultBlockState();
		BlockState stem = EdenBlocks.BALLOON_MUSHROOM_STEM.defaultBlockState();
		
		byte height = (byte) MHelper.randRange(8, 12, random);
		makeTrunk(level, center, p.set(center), log, height);
		makeRoots(level, center, p, log, bark, stem, random);
		makeBranches(level, center, p, stem, height, random);
		makeCap(level, p.set(center).setY(p.getY() + height), block, height, random);
		
		return true;
	}
	
	private void makeTrunk(WorldGenLevel level, BlockPos center, MutableBlockPos p, BlockState log, byte height) {
		for (byte i = 0; i < height; i++) {
			setBlock(level, p, log);
			setBlock(level, p.setX(center.getX() + 1), log);
			setBlock(level, p.setZ(center.getZ() + 1), log);
			setBlock(level, p.setX(center.getX()), log);
			p.setZ(center.getZ()).setY(p.getY() + 1);
		}
	}
	
	private void makeCap(WorldGenLevel level, MutableBlockPos p, BlockState block, byte height, Random random) {
		BlockState bottom = block.setValue(EdenBlockProperties.NATURAL, true);
		BlockState hymenophore = EdenBlocks.BALLOON_MUSHROOM_HYMENOPHORE.defaultBlockState();
		BlockState hymenophoreBottom = hymenophore.setValue(EdenBlockProperties.QUAD_SHAPE, QuadShape.BOTTOM);
		BlockState hymenophoreMiddle = hymenophore.setValue(EdenBlockProperties.QUAD_SHAPE, QuadShape.MIDDLE);
		BlockState hymenophoreSmall = hymenophore.setValue(EdenBlockProperties.QUAD_SHAPE, QuadShape.SMALL);
		BlockState hymenophoreTop = hymenophore.setValue(EdenBlockProperties.QUAD_SHAPE, QuadShape.TOP);
		
		List<BlockPos> updateBlocks = new ArrayList(128);
		BlockPos center = p.immutable();
		byte radius = (byte) (height >> 1);
		byte startY = (byte) (-radius * 0.5F);
		byte r2 = (byte) (radius * radius);
		
		for (byte y = startY; y <= radius; y++) {
			BlockState state = y == startY ? bottom : block;
			byte y2 = (byte) (y * y);
			for (byte x = (byte) -radius; x <= radius; x++) {
				float x2 = (x - 0.5F) * (x - 0.5F);
				for (byte z = (byte) -radius; z <= radius; z++) {
					float z2 = (z - 0.5F) * (z - 0.5F);
					if (x2 + y2 + z2 <= r2) {
						setBlock(level, p.set(center).move(x, y - startY, z), state);
						updateBlocks.add(p.immutable());
						if (y == startY && random.nextInt(4) > 0) {
							byte length = (byte) MHelper.randRange(1, 3, random);
							makeVine(level, p.mutable().setY(p.getY() - 1), length, hymenophoreSmall, hymenophoreBottom, hymenophoreMiddle, hymenophoreTop);
						}
					}
				}
			}
		}
		
		updateBlocks.forEach(pos -> {
			BlockState s = level.getBlockState(pos);
			s = s.getBlock().updateShape(s, Direction.UP, AIR, level, pos, pos);
			BlocksHelper.setWithoutUpdate(level, pos, s);
		});
	}
	
	private void makeVine(WorldGenLevel level, MutableBlockPos pos, byte length, BlockState small, BlockState bottom, BlockState middle, BlockState top) {
		if (!level.getBlockState(pos).isAir()) return;
		if (length == 1) {
			BlocksHelper.setWithoutUpdate(level, pos, small);
			return;
		}
		byte maxLength = (byte) (length - 1);
		for (byte i = 0; i < length; i++) {
			BlockState state = i == maxLength ? bottom : i == 0 ? top : middle;
			BlocksHelper.setWithoutUpdate(level, pos, state);
			pos.setY(pos.getY() - 1);
			if (!level.getBlockState(pos).isAir()) {
				state = i == 0 ? small : bottom;
				BlocksHelper.setWithoutUpdate(level, pos.setY(pos.getY() + 1), state);
				return;
			}
		}
	}
	
	private void makeRoots(WorldGenLevel level, BlockPos center, MutableBlockPos p, BlockState log, BlockState bark, BlockState stem, Random random) {
		byte[][] mask = new byte[4][4];
		mask[1][1] = 4;
		mask[1][2] = 4;
		mask[2][1] = 4;
		mask[2][2] = 4;
		
		for (byte i = 0; i < 2; i++) {
			if (random.nextBoolean()) {
				byte h = (byte) random.nextInt(3);
				byte h2 = (byte) -(h + MHelper.randRange(2, 3, random));
				makeLine(level, p.set(center).move(i, h, -1), bark, log, h2);
				mask[i + 1][0] = (byte) (h + 1);
			}
			if (random.nextBoolean()) {
				byte h = (byte) random.nextInt(3);
				byte h2 = (byte) -(h + MHelper.randRange(2, 3, random));
				makeLine(level, p.set(center).move(i, h, 2), bark, log, h2);
				mask[i + 1][3] = (byte) (h + 1);
			}
			if (random.nextBoolean()) {
				byte h = (byte) random.nextInt(3);
				byte h2 = (byte) -(h + MHelper.randRange(2, 3, random));
				makeLine(level, p.set(center).move(-1, h, i), bark, log, h2);
				mask[0][i + 1] = (byte) (h + 1);
			}
			if (random.nextBoolean()) {
				byte h = (byte) random.nextInt(3);
				byte h2 = (byte) -(h + MHelper.randRange(2, 3, random));
				makeLine(level, p.set(center).move(2, h, i), bark, log, h2);
				mask[3][i + 1] = (byte) (h + 1);
			}
		}
		
		if (random.nextBoolean()) {
			byte h = (byte) MHelper.randRange(1, 2, random);
			byte h2 = (byte) (-h - 2);
			makeLine(level, p.set(center).move(-1, h - 1, -1), bark, log, h2);
			mask[0][0] = h;
		}
		if (random.nextBoolean()) {
			byte h = (byte) MHelper.randRange(1, 2, random);
			byte h2 = (byte) (-h - 2);
			makeLine(level, p.set(center).move(2, h - 1, -1), bark, log, h2);
			mask[3][0] = h;
		}
		if (random.nextBoolean()) {
			byte h = (byte) MHelper.randRange(1, 2, random);
			byte h2 = (byte) (-h - 2);
			makeLine(level, p.set(center).move(2, h - 1, 2), bark, log, h2);
			mask[3][3] = h;
		}
		if (random.nextBoolean()) {
			byte h = (byte) MHelper.randRange(1, 2, random);
			byte h2 = (byte) (-h - 2);
			makeLine(level, p.set(center).move(-1, h - 1, 2), bark, log, h2);
			mask[0][3] = h;
		}
		
		BlockState branch = EdenBlocks.BALLOON_MUSHROOM_BRANCH.defaultBlockState().setValue(BlockStateProperties.DOWN, true);
		Direction[] dirs = BlocksHelper.makeHorizontal();
		for (int x = -2; x < 4; x++) {
			for (int z = -2; z < 4; z++) {
				if (random.nextInt(4) == 0) continue;
				MHelper.shuffle(dirs, random);
				for (Direction dir: dirs) {
					byte h = getConnection(x, z, dir, mask);
					if (h > 0) {
						if (h > 1) h = (byte) MHelper.randRange(1, h, random);
						BlockState start = branch.setValue(EdenBlockProperties.DIRECTIONS[dir.get3DDataValue()], true);
						makeLine(level, p.set(center).move(x, h - 1, z), start, stem, (byte) -h);
						break;
					}
				}
			}
		}
	}
	
	private void makeBranches(WorldGenLevel level, BlockPos center, MutableBlockPos p, BlockState stem, byte height, Random random) {
		BlockState branch = EdenBlocks.BALLOON_MUSHROOM_BRANCH.defaultBlockState().setValue(BlockStateProperties.UP, true);
		for (byte i = 0; i < 2; i++) {
			if (random.nextInt(4) > 0) {
				byte dy = (byte) MHelper.randRange(2, 4, random);
				byte dx = (byte) random.nextInt(2);
				singleBranch(level, p.set(center).move(i, height - dy, -1), Direction.NORTH, dx, dy, stem, branch);
			}
			if (random.nextInt(4) > 0) {
				byte dy = (byte) MHelper.randRange(2, 4, random);
				byte dx = (byte) random.nextInt(2);
				singleBranch(level, p.set(center).move(i, height - dy, 2), Direction.SOUTH, dx, dy, stem, branch);
			}
			if (random.nextInt(4) > 0) {
				byte dy = (byte) MHelper.randRange(2, 4, random);
				byte dx = (byte) random.nextInt(2);
				singleBranch(level, p.set(center).move(-1, height - dy, i), Direction.WEST, dx, dy, stem, branch);
			}
			if (random.nextInt(4) > 0) {
				byte dy = (byte) MHelper.randRange(2, 4, random);
				byte dx = (byte) random.nextInt(2);
				singleBranch(level, p.set(center).move(2, height - dy, i), Direction.EAST, dx, dy, stem, branch);
			}
		}
	}
	
	private void singleBranch(WorldGenLevel level, MutableBlockPos start, Direction dir, byte dxz, byte dy, BlockState stem, BlockState branch) {
		branch = branch.setValue(EdenBlockProperties.DIRECTIONS[dir.getOpposite().get3DDataValue()], true);
		if (dxz == 0) {
			makeLine(level, start, branch, stem, dy);
		}
		else {
			BalloonMushroomStemState state = dir.getAxis() == Axis.Z ? BalloonMushroomStemState.NORTH_SOUTH : BalloonMushroomStemState.EAST_WEST;
			BlockState side = stem.setValue(EdenBlockProperties.BALLOON_MUSHROOM_STEM, state);
			for (byte i = 0; i < dxz; i++) {
				setBlock(level, start, side);
				start.move(dir);
			}
			makeLine(level, start, branch, stem, dy);
		}
	}
	
	private byte getConnection(int x, int z, Direction dir, byte[][] mask) {
		x += dir.getStepX() + 1;
		z += dir.getStepZ() + 1;
		if (x >= 0 && x < 4 && z >= 0 && z < 4) return mask[x][z];
		return 0;
	}
	
	private void makeLine(WorldGenLevel level, MutableBlockPos p, BlockState start, BlockState line, byte height) {
		byte delta = (byte) (height > 0 ? 1 : -1);
		setBlock(level, p, start);
		for (byte i = delta; i != height; i += delta) {
			setBlock(level, p.setY(p.getY() + delta), line);
		}
	}
	
	private void setBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
		BlockState place = level.getBlockState(pos);
		if (place.getMaterial().isReplaceable() || place.is(EdenBlocks.BALLOON_MUSHROOM_SMALL) || place.getMaterial().equals(Material.PLANT)) {
			BlocksHelper.setWithoutUpdate(level, pos, state);
		}
	}
}
