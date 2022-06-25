package paulevs.edenring.mixin.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.generator.EdenDensityFunction;

@Mixin(DensityFunctions.class)
public class DensityFunctionsMixin {
	/*@Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lcom/mojang/serialization/Codec;", at = @At("HEAD"))
	private static void eden_onDensityInit(Registry<Codec<? extends DensityFunction>> registry, CallbackInfoReturnable<Codec<? extends DensityFunction>> info) {
		Registry.register(registry, EdenRing.makeID("islands"), EdenDensityFunction.CODEC.codec());
	}*/
}
