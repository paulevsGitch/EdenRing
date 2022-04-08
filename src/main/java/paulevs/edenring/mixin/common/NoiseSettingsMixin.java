package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.edenring.interfaces.EdenTargetChecker;

@Mixin(NoiseSettings.class)
public class NoiseSettingsMixin implements EdenTargetChecker {
	@Unique boolean eden_isEdenTarget;
	
	@Unique
	@Override
	public void eden_setTarget(boolean target) {
		eden_isEdenTarget = target;
	}
	
	@Unique
	@Override
	public boolean eden_isTarget() {
		return eden_isEdenTarget;
	}
}
