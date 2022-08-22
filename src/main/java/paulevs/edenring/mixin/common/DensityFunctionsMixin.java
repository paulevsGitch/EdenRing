package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.DensityFunctions;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DensityFunctions.class)
public class DensityFunctionsMixin {
	/*@Inject(method = "bootstrap(Lnet/minecraft/core/Registry;)Lcom/mojang/serialization/Codec;", at = @At("HEAD"))
	private static void eden_onDensityInit(Registry<Codec<? extends DensityFunction>> registry, CallbackInfoReturnable<Codec<? extends DensityFunction>> info) {
		Registry.register(registry, EdenRing.makeID("islands"), EdenDensityFunction.CODEC.codec());
	}*/
}
