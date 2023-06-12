package paulevs.edenring.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.betterx.bclib.util.MHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
	protected ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
		super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
	}
	
	@Inject(method = "getSkyDarken", at = @At("HEAD"), cancellable = true)
	private void eden_getSkyDarken(float timeDelta, CallbackInfoReturnable<Float> info) {
		if (dimension().equals(EdenRing.EDEN_RING_KEY)) {
			float time = getTimeOfDay(timeDelta);
			float light = 1.0f - (Mth.cos(time * MHelper.PI2) * 2.0f + 0.2f);
			light = Mth.clamp(light, 0.0f, 1.0f);
			light = (1.0F - light) * (1.0F - (getThunderLevel(timeDelta) * 5.0F) / 16.0F);
			info.setReturnValue(light * 0.8F + 0.2F);
		}
	}
}
