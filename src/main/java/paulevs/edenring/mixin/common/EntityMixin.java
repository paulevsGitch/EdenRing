package paulevs.edenring.mixin.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenPortable;

@Mixin(Entity.class)
public class EntityMixin implements EdenPortable {
	private int eden_PortalTimeout = 0;
	
	@Inject(method = "checkBelowWorld", at = @At("HEAD"), cancellable = true)
	public void eden_checkOutOfWorld(CallbackInfo info) {
		Entity entity = Entity.class.cast(this);
		if (entity.level().dimension() == EdenRing.EDEN_RING_KEY && entity.getY() > entity.level().getMaxBuildHeight() + 64) {
			onBelowWorld();
			info.cancel();
		}
	}
	
	@Shadow
	protected void onBelowWorld() {}
	
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
	
	@Inject(method = "save", at = @At("HEAD"))
	private void eden_saveEntity(CompoundTag compoundTag, CallbackInfoReturnable<Boolean> info) {
		if (getPortalTimeout() > 0) {
			compoundTag.putInt("edenPortalTimeout", getPortalTimeout());
		}
	}
	
	@Inject(method = "load", at = @At("HEAD"))
	private void eden_loadEntity(CompoundTag compoundTag, CallbackInfo info) {
		if (compoundTag != null && compoundTag.contains("edenPortalTimeout")) {
			setPortalTimeout(compoundTag.getInt("edenPortalTimeout"));
		}
	}
}
