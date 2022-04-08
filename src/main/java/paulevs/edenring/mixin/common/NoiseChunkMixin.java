package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.EdenTargetChecker;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin {
	@Final @Shadow private NoiseSettings noiseSettings;
	
	@Inject(
		method = "fillAllDirectly([DLnet/minecraft/world/level/levelgen/DensityFunction;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	public void eden_fillArray(double[] ds, DensityFunction densityFunction, CallbackInfo info) {
		if (EdenTargetChecker.class.cast(noiseSettings).eden_isTarget()) {
			info.cancel();
		}
	}
}
