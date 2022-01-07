package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.edenring.interfaces.TargetChecker;

@Mixin(NoiseSampler.class)
public class NoiseSamplerMixin implements TargetChecker {
	private boolean eden_isTargetNoiseSampler;
	
	@Override
	public void setTarget(boolean target) {
		eden_isTargetNoiseSampler = target;
	}
	
	@Override
	public boolean isTarget() {
		return eden_isTargetNoiseSampler;
	}
}
