package paulevs.edenring.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenTargetChecker;
import paulevs.edenring.world.generator.CaveGenerator;
import paulevs.edenring.world.generator.EdenBiomeSource;
import paulevs.edenring.world.generator.MultiThreadGenerator;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void eden_onServerInit(MinecraftServer minecraftServer, Executor executor, LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey resourceKey, Holder holder, ChunkProgressListener chunkProgressListener, ChunkGenerator chunkGenerator, boolean bl, long seed, List list, boolean bl2, CallbackInfo info) {
		ServerLevel level = ServerLevel.class.cast(this);
		
		BiomeSource source = chunkGenerator.getBiomeSource();
		if (source instanceof EdenBiomeSource) {
			EdenBiomeSource edenSource = EdenBiomeSource.class.cast(source);
			CaveGenerator.init(seed, edenSource);
			edenSource.setSeed(seed);
		}
		
		if (level.dimension().equals(EdenRing.EDEN_RING_KEY)) {
			MultiThreadGenerator.init(seed, chunkGenerator.climateSampler());
			EdenTargetChecker.class.cast(chunkGenerator).eden_setTarget(true);
		}
	}
}
