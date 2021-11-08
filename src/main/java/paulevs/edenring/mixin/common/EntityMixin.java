package paulevs.edenring.mixin.common;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenPortable;

@Mixin(Entity.class)
public class EntityMixin implements EdenPortable {
	private int eden_PortalTimeout = 0;
	
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
	
	@Override
	public int getPortalTimeout() {
		return eden_PortalTimeout;
	}
	
	@Override
	public void setPortalTimeout(int timeout) {
		eden_PortalTimeout = timeout;
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void eden_entityTick(CallbackInfo info) {
		int time = getPortalTimeout();
		if (time > 0) {
			setPortalTimeout(time - 1);
		}
	}
}
