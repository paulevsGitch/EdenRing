package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.edenring.interfaces.EdenTargetChecker;

@Mixin(NoiseSampler.class)
public class NoiseSamplerMixin implements EdenTargetChecker {
	private boolean eden_isTargetNoiseSampler;
	
	@Override
	public void eden_setTarget(boolean target) {
		eden_isTargetNoiseSampler = target;
	}
	
	@Override
	public boolean eden_isTarget() {
		return eden_isTargetNoiseSampler;
	}
}
