package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "checkOutOfWorld", at = @At("HEAD"), cancellable = true)
	public void eden_checkOutOfWorld(CallbackInfo info) {
		Entity entity = Entity.class.cast(this);
		if (entity.level.dimension() == EdenRing.EDEN_RING_KEY && entity.getY() > entity.level.getMaxBuildHeight() + 64) {
			outOfWorld();
			info.cancel();
		}
	}
	
	@Shadow
	protected void outOfWorld() {}
}
