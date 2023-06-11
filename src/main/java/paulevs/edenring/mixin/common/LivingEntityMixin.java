package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.GravityController;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08D))
	private double eden_changeGravity(double gravity) {
		LivingEntity entity = LivingEntity.class.cast(this);
		if (entity.level().dimension() == EdenRing.EDEN_RING_KEY) {
			gravity *= GravityController.getGravityMultiplier(entity.getY());
		}
		double gravilite = GravityController.getGraviliteMultiplier(entity);
		if (gravilite == 1) {
			gravilite = GravityController.getCompressorMultiplier(entity);
		}
		gravity *= gravilite;
		return gravity;
	}
	
	@Inject(method = "checkFallDamage", at = @At("TAIL"))
	private void eden_checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos, CallbackInfo info) {
		LivingEntity entity = LivingEntity.class.cast(this);
		if (entity.level().dimension().equals(EdenRing.EDEN_RING_KEY)) {
			entity.fallDistance *= GravityController.getGravityMultiplier(entity.getY());
		}
		double gravilite = GravityController.getGraviliteMultiplier(entity);
		if (gravilite == 1) {
			gravilite = GravityController.getCompressorMultiplier(entity);
		}
		entity.fallDistance *= gravilite;
	}
}
