package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.registries.EdenBlocks;

@Mixin(AmethystClusterBlock.class)
public class AmethystClusterBlockMixin {
	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	private void eden_canCrystalSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> info) {
		if (blockState.getValue(BlockStateProperties.FACING) == Direction.UP) {
			if (levelReader.getBlockState(blockPos.below()).is(EdenBlocks.PORTAL_BLOCK)) {
				info.setReturnValue(true);
			}
		}
	}
}
