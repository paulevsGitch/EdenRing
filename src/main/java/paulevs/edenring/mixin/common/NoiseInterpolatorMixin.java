package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.TargetChecker;
import paulevs.edenring.world.generator.TerrainGenerator;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public class NoiseInterpolatorMixin {
	@Final
	@Shadow(aliases = "this$0")
	private NoiseChunk this$0;
	
	@Inject(method = "fillSlice", at = @At("HEAD"), cancellable = true)
	private void eden_fillSlice(double[][] data, int x, CallbackInfo info) {
		NoiseChunkAccessor accessor = NoiseChunkAccessor.class.cast(this$0);
		if (!TargetChecker.class.cast(accessor.eden_getSampler()).isTarget()) {
			return;
		}
		NoiseSettings noiseSettings = accessor.eden_getNoiseSettings();
		
		final int sizeY = noiseSettings.getCellHeight();
		final int sizeXZ = noiseSettings.getCellWidth();
		final int cellsXZ = accessor.eden_getCellCountXZ() + 1;
		final int firstCellZ = accessor.eden_getFirstCellZ();
		
		x *= sizeXZ;
		
		for (int cellXZ = 0; cellXZ < cellsXZ; ++cellXZ) {
			int z = (firstCellZ + cellXZ) * sizeXZ;
			TerrainGenerator.fillTerrainDensity(data[cellXZ], x, z, sizeXZ, sizeY);
		}
		
		info.cancel();
	}
}
