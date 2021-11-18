package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.EdenPortal;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {
	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void eden_portalOpen(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> info) {
		if (useOnContext.getClickedFace() == Direction.UP) {
			BlockPos pos = useOnContext.getClickedPos();
			Level level = useOnContext.getLevel();
			if (correctDimension(level.dimension()) && level.getBlockState(pos).is(Blocks.GOLD_BLOCK)) {
				if (EdenPortal.checkNewPortal(level, pos.above())) {
					info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
					EdenPortal.buildPortal(level, pos.above());
				}
			}
		}
	}
	
	private boolean correctDimension(ResourceKey dimension) {
		return dimension.equals(Level.OVERWORLD) || dimension.equals(EdenRing.EDEN_RING_KEY);
	}
}
