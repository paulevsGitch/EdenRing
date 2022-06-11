package paulevs.edenring.mixin.common;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.interfaces.EdenTargetChecker;
import paulevs.edenring.noise.InterpolationCell;
import paulevs.edenring.world.generator.CaveGenerator;
import paulevs.edenring.world.generator.TerrainFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin implements EdenTargetChecker {
	private boolean eden_isTarget;
	@Shadow @Final protected Holder<NoiseGeneratorSettings> settings;
	
	@Inject(
		method = "fillFromNoise(Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/levelgen/blending/Blender;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)Ljava/util/concurrent/CompletableFuture;",
		at = @At("HEAD"), cancellable = true
	)
	private void eden_fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> info) {
		if (eden_isTarget()) {
			info.setReturnValue(CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> {
				synchronized (chunkAccess) {
					InterpolationCell cellTerrain = TerrainFiller.fill(chunkAccess);
					CaveGenerator.carve(chunkAccess, cellTerrain);
				}
				return chunkAccess;
			}), Util.backgroundExecutor()));
		}
	}
	
	@Override
	public void eden_setTarget(boolean target) {
		EdenTargetChecker.class.cast(settings.value().noiseSettings()).eden_setTarget(target);
		eden_isTarget = target;
	}
	
	@Override
	public boolean eden_isTarget() {
		return eden_isTarget;
	}
}
