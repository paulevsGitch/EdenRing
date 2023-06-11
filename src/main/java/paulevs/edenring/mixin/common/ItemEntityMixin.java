package paulevs.edenring.mixin.common;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.GravityController;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = -0.04D))
	private double eden_changeGravity(double gravity) {
		ItemEntity entity = ItemEntity.class.cast(this);
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
}
