package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyConstant(method = "travel", constant = @Constant(doubleValue = 0.08D))
	private double eden_changeGravity(double gravity) {
		LivingEntity entity = LivingEntity.class.cast(this);
		if (entity.level.dimension() == EdenRing.EDEN_RING_KEY) {
			gravity *= eden_getGravityMultiplier(entity.getY());
		}
		gravity *= eden_getGraviliteMultiplier(entity);
		return gravity;
	}
	
	@Inject(method = "checkFallDamage", at = @At("TAIL"))
	private void eden_checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos, CallbackInfo info) {
		LivingEntity entity = LivingEntity.class.cast(this);
		if (entity.level.dimension().equals(EdenRing.EDEN_RING_KEY)) {
			entity.fallDistance *= eden_getGravityMultiplier(entity.getY());
		}
		entity.fallDistance *= eden_getGraviliteMultiplier(entity);
	}
	
	private double eden_getGravityMultiplier(double y) {
		return Mth.lerp(Math.abs(y - 128.0) * 0.007, 1.0, 0.2);
	}
	
	private double eden_getGraviliteMultiplier(LivingEntity entity) {
		MutableBlockPos pos = entity.blockPosition().mutable();
		int dist = 8;
		for (int i = 0; i < 8; i++) {
			if (entity.level.getBlockState(pos).is(EdenBlocks.GRAVILITE)) {
				dist = i;
				break;
			}
			pos.setY(pos.getY() - 1);
		}
		if (dist == 8) {
			return 1.0;
		}
		float delta = Mth.clamp((dist + (float) (entity.getY() - (int) entity.getY())) / 8F, 0, 1);
		return Mth.lerp(delta, 0.1, 1.0);
	}
}
