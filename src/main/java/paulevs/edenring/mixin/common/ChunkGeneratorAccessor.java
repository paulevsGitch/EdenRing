package paulevs.edenring.mixin.common;

import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
	//@Mutable
	//@Accessor("settings")
	//void eden_setSettings(StructureSettings settings);
}
