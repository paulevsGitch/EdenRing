package paulevs.edenring.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.EdenTargetChecker;
import paulevs.edenring.noise.InterpolationCell;
import paulevs.edenring.world.generator.CaveGenerator;
import paulevs.edenring.world.generator.TerrainFiller;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin implements EdenTargetChecker {
	private boolean eden_isTarget;
	@Shadow @Final protected Holder<NoiseGeneratorSettings> settings;
	
	@Inject(
		method = "buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureFeatureManager;Lnet/minecraft/world/level/chunk/ChunkAccess;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Holder;value()Ljava/lang/Object;")
	)
	private void eden_carveBeforeSurface(WorldGenRegion worldGenRegion, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfo ci) {
		if (eden_isTarget()) {
			InterpolationCell cellTerrain = TerrainFiller.fill(chunkAccess);
			CaveGenerator.carve(chunkAccess, cellTerrain);
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
