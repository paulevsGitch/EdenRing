package paulevs.edenring.world.features.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.registries.EdenBlocks;

public class AquatusFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		RandomSource random = featurePlaceContext.random();
		
		int side = MHelper.randRange(1, 3, random);
		int min = -(side >> 1);
		int max = side + min;
		
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = min; x < max; x++) {
			pos.setX(center.getX() + x);
			for (int z = min; z < max; z++) {
				pos.setZ(center.getZ() + z);
				
				pos.setY(center.getY() - 1);
				if (!isTerrain(level.getBlockState(pos))) {
					return false;
				}
				
				pos.setY(center.getY());
				if (!canReplace(level.getBlockState(pos))) {
					return false;
				}
			}
		}
		
		int minDec = min - 1;
		pos.setY(center.getY() + 2);
		for (int x = minDec; x <= max; x++) {
			pos.setX(center.getX() + x);
			for (int z = minDec; z <= max; z++) {
				if (x != minDec || x != max || z != minDec || z != max) {
					pos.setZ(center.getZ() + z);
					if (!canReplace(level.getBlockState(pos))) {
						return false;
					}
				}
			}
		}
		
		BlockState roots = EdenBlocks.AQUATUS_ROOTS.defaultBlockState().setValue(BlockStateProperties.UP, false);
		BlockState leaves = EdenBlocks.AQUATUS_ROOTS.defaultBlockState().setValue(BlockStateProperties.UP, true);
		BlockState block = EdenBlocks.AQUATUS_BLOCK.defaultBlockState();
		BlockState water = Blocks.WATER.defaultBlockState();
		
		for (int x = min; x < max; x++) {
			pos.setX(center.getX() + x);
			for (int z = min; z < max; z++) {
				pos.setZ(center.getZ() + z);
				
				pos.setY(center.getY());
				setBlock(level, pos, roots);
				
				pos.setY(center.getY() + 1);
				setBlock(level, pos, block);
				
				pos.setY(center.getY() + 2);
				setBlock(level, pos, water);
			}
		}
		
		for (int i = min; i < max; i++) {
			pos.setZ(center.getZ() + i);
			
			pos.setX(center.getX() + minDec);
			pos.setY(center.getY() + 1);
			setBlock(level, pos, leaves);
			pos.setY(center.getY() + 2);
			setBlock(level, pos, block);
			
			pos.setX(center.getX() + max);
			pos.setY(center.getY() + 1);
			setBlock(level, pos, leaves);
			pos.setY(center.getY() + 2);
			setBlock(level, pos, block);
			
			pos.setX(center.getX() + i);
			
			pos.setZ(center.getZ() + minDec);
			pos.setY(center.getY() + 1);
			setBlock(level, pos, leaves);
			pos.setY(center.getY() + 2);
			setBlock(level, pos, block);
			
			pos.setZ(center.getZ() + max);
			pos.setY(center.getY() + 1);
			setBlock(level, pos, leaves);
			pos.setY(center.getY() + 2);
			setBlock(level, pos, block);
		}
		
		return true;
	}
	
	private boolean isTerrain(BlockState state) {
		return state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || state.is(Blocks.DIRT) || state.getBlock() instanceof GrassBlock;
	}
	
	private boolean canReplace(BlockState state) {
		return state.isAir() || state.is(EdenBlocks.AQUATUS_BLOCK) || BlocksHelper.replaceableOrPlant(state);
	}
	
	private void setBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
		if (canReplace(level.getBlockState(pos))) {
			BlocksHelper.setWithoutUpdate(level, pos, state);
		}
	}
}
