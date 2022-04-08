package paulevs.edenring.mixin.common;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.EdenTargetChecker;
import paulevs.edenring.world.generator.CaveGenerator;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin implements EdenTargetChecker {
	private boolean eden_isTarget;
	
	@Inject(
		method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;")
	)
	private void eden_carveBeforeSurface(WorldGenRegion worldGenRegion, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfo ci) {
		if (eden_isTarget()) {
			CaveGenerator.carve(chunkAccess);
		}
	}
	
	@Override
	public void eden_setTarget(boolean target) {
		eden_isTarget = target;
	}
	
	@Override
	public boolean eden_isTarget() {
		return eden_isTarget;
	}
}
