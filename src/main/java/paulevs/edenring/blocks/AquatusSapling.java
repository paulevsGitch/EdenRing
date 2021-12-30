package paulevs.edenring.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenFeatures;
import ru.bclib.blocks.FeatureSaplingBlock;

public class AquatusSapling extends FeatureSaplingBlock {
	public AquatusSapling() {
		super((state) -> EdenFeatures.AQUATUS.getFeature());
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
		return state.is(Blocks.SAND) || state.is(Blocks.GRAVEL) || super.mayPlaceOn(state, world, pos);
	}
}
