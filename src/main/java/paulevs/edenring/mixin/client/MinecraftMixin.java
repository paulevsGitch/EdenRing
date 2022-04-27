package paulevs.edenring.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.gui.GuideBookScreen;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	public LocalPlayer player;
	
	@Shadow
	public ClientLevel level;
	
	@Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
	private void eden_getSituationalMusic(CallbackInfoReturnable<Music> info) {
		if (player != null && level != null && level.dimension().equals(EdenRing.EDEN_RING_KEY)) {
			Biome biome = level.getBiomeManager().getNoiseBiomeAtPosition(player.blockPosition()).value();
			info.setReturnValue(biome.getBackgroundMusic().orElse(Musics.GAME));
		}
	}
	
	@Inject(method = "reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
	private void eden_reloadResourcePacks(boolean bl, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		GuideBookScreen.clearCache();
	}
}
