package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.blocks.MossyStoneBlock;

@Mixin(BushBlock.class)
public class BushBlockMixin {
	@Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
	private void eden_mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
		if (blockState.getBlock() instanceof GrassBlock || blockState.getBlock() instanceof MossyStoneBlock) {
			info.setReturnValue(true);
		}
	}
}
