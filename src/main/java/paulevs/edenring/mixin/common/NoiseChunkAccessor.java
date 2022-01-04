package paulevs.edenring.mixin.common;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NoiseChunk.class)
public interface NoiseChunkAccessor {
	@Accessor("noiseSettings")
	NoiseSettings eden_getNoiseSettings();
	
	@Accessor("cellCountXZ")
	int eden_getCellCountXZ();
	
	@Accessor("firstCellZ")
	int eden_getFirstCellZ();
}
