package paulevs.edenring.mixin.common;

import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.generator.CaveGenerator;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {
	@Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Ljava/util/function/Supplier;get()Ljava/lang/Object;"))
	private void eden_carveBeforeSurface(WorldGenRegion worldGenRegion, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfo ci) {
		Registry<DimensionType> registry = worldGenRegion.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
		DimensionType dimension = registry.get(EdenRing.EDEN_RING_TYPE_KEY);
		if (worldGenRegion.dimensionType() == dimension) {
			CaveGenerator.carve(chunkAccess);
		}
	}
}
