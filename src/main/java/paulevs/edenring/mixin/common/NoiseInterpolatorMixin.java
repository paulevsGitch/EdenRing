package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.interfaces.EdenTargetChecker;
import paulevs.edenring.world.generator.MultiThreadGenerator;
import paulevs.edenring.world.generator.TerrainGenerator;

@Mixin(NoiseChunk.NoiseInterpolator.class)
public class NoiseInterpolatorMixin {
	// TODO do something with interpolator
	/*@Final
	@Shadow(aliases = "this$0")
	private NoiseChunk this$0;
	
	@Inject(method = "fillSlice", at = @At("HEAD"), cancellable = true)
	private void eden_fillSlice(double[][] data, int x, CallbackInfo info) {
		NoiseChunkAccessor accessor = NoiseChunkAccessor.class.cast(this$0);
		if (!EdenTargetChecker.class.cast(accessor.eden_getSampler()).eden_isTarget()) {
			return;
		}
		NoiseSettings noiseSettings = accessor.eden_getNoiseSettings();
		
		final int sizeY = noiseSettings.getCellHeight();
		final int sizeXZ = noiseSettings.getCellWidth();
		final int cellsXZ = accessor.eden_getCellCountXZ() + 1;
		final int firstCellZ = accessor.eden_getFirstCellZ();
		
		x *= sizeXZ;
		
		float[] floatBuffer = new float[data[0].length];
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		for (int cellXZ = 0; cellXZ < cellsXZ; ++cellXZ) {
			int z = (firstCellZ + cellXZ) * sizeXZ;
			generator.fillTerrainDensity(data[cellXZ], x, z, sizeXZ, sizeY, floatBuffer);
		}
		
		info.cancel();
	}*/
}
