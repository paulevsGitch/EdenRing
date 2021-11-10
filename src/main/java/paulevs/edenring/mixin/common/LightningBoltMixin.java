package paulevs.edenring.mixin.common;

import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import paulevs.edenring.EdenRing;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 10000.0F))
	private float eden_changeSoundDistance(float distance) {
		LightningBolt bolt = LightningBolt.class.cast(this);
		return bolt.level.dimension().equals(EdenRing.EDEN_RING_KEY) ? 100.0F : distance;
	}
}
