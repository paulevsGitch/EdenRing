package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseChunk.NoiseFiller;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.TargetChecker;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin implements TargetChecker {
	private boolean eden_isTargetGenerator;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void be_onNoiseChunkInit(int i, int j, int k, NoiseSampler noiseSampler, int l, int m, NoiseFiller noiseFiller, NoiseGeneratorSettings noiseGeneratorSettings, FluidPicker fluidPicker, Blender blender, CallbackInfo info) {
		setTarget(TargetChecker.class.cast(noiseSampler).isTarget());
	}
	
	@Override
	public void setTarget(boolean target) {
		eden_isTargetGenerator = target;
	}
	
	@Override
	public boolean isTarget() {
		return eden_isTargetGenerator;
	}
}
