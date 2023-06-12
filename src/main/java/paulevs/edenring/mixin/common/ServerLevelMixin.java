package paulevs.edenring.mixin.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
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
	private void eden_onServerInit(
			MinecraftServer minecraftServer,
			Executor executor,
			LevelStorageSource.LevelStorageAccess levelStorageAccess,
			ServerLevelData serverLevelData,
			ResourceKey<Level> resourceKey,
			LevelStem levelStem,
			ChunkProgressListener chunkProgressListener,
			boolean bl,
			long seed,
			List<CustomSpawner> list,
			boolean bl2,
			@Nullable RandomSequences randomSequences,
			CallbackInfo ci
	) {
		ServerLevel level = ServerLevel.class.cast(this);
		
		ChunkGenerator chunkGenerator = levelStem.generator();
		BiomeSource source = chunkGenerator.getBiomeSource();
		if (source instanceof EdenBiomeSource) {
			EdenBiomeSource.class.cast(source).setSeed(seed);
			CaveGenerator.init(seed);
		}
		
		if (level.dimension().equals(EdenRing.EDEN_RING_KEY)) {
			MultiThreadGenerator.init(seed);
			EdenTargetChecker.class.cast(chunkGenerator).eden_setTarget(true);
		}
	}
}
