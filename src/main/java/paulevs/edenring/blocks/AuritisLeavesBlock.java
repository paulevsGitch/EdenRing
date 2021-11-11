package paulevs.edenring.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenParticles;
import ru.bclib.blocks.BaseLeavesBlock;

import java.util.Random;

public class AuritisLeavesBlock extends BaseLeavesBlock {
	public AuritisLeavesBlock() {
		super(EdenBlocks.AURITIS_SAPLING, MaterialColor.GOLD);
	}
	
	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos pos, Random random) {
		if (random.nextInt(32) == 0 && level.getBlockState(pos.below()).isAir()) {
			float dx = random.nextFloat();
			float dz = random.nextFloat();
			level.addParticle(EdenParticles.AURITIS_LEAF_PARTICLE, pos.getX() + dx, pos.getY() - 0.01F, pos.getZ() + dz, 0, 0, 0);
		}
	}
}
