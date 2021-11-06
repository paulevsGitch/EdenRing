package paulevs.edenring.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class BrainTreeFeature extends DefaultFeature {
	private static final BlockState[] TYPES = new BlockState[3];
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		if (TYPES[0] == null) {
			TYPES[0] = EdenBlocks.BRAIN_TREE_BLOCK_IRON.defaultBlockState();
			TYPES[1] = EdenBlocks.BRAIN_TREE_BLOCK_COPPER.defaultBlockState();
			TYPES[2] = EdenBlocks.BRAIN_TREE_BLOCK_GOLD.defaultBlockState();
		}
		
		BlockState brain = TYPES[random.nextInt(3)];
		BlockState stem = Blocks.OAK_LOG.defaultBlockState();
		
		MutableBlockPos pos = center.mutable();
		int h = MHelper.randRange(2, 4, random);
		
		BlocksHelper.setWithoutUpdate(level, pos, stem);
		for (int i = 1; i < h; i++) {
			pos.setY(center.getY() + i);
			if (canReplace(level.getBlockState(pos))) {
				BlocksHelper.setWithoutUpdate(level, pos, stem);
			}
		}
		
		for (int y = 0; y < 4; y++) {
			pos.setY(center.getY() + h + y);
			for (int x = -1; x < 2; x++) {
				pos.setX(center.getX() + x);
				for (int z = -1; z < 2; z++) {
					pos.setZ(center.getZ() + z);
					if (canReplace(level.getBlockState(pos))) {
						BlocksHelper.setWithoutUpdate(level, pos, brain);
					}
				}
			}
		}
		
		for (int y = 0; y < 2; y++) {
			pos.setY(center.getY() + h + y + 1);
			for (int x = -2; x < 3; x++) {
				pos.setX(center.getX() + x);
				int ax = Mth.abs(x);
				for (int z = -2; z < 3; z++) {
					pos.setZ(center.getZ() + z);
					int az = Mth.abs(z);
					if ((ax != 2 || az != 2 || ax != az) && canReplace(level.getBlockState(pos))) {
						BlocksHelper.setWithoutUpdate(level, pos, brain);
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean canReplace(BlockState state) {
		return state.isAir() || state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT);
	}
}
