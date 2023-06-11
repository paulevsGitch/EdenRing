package paulevs.edenring.blocks.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.betterx.bclib.util.BlocksHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenPortable;
import paulevs.edenring.registries.EdenBlockEntities;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.world.EdenPortal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdenPortalBlockEntity extends BlockEntity {
	private int checkTicks;
	private long ticks;
	private AABB box;
	
	public EdenPortalBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(EdenBlockEntities.EDEN_PORTAL, blockPos, blockState);
	}
	
	@Environment(EnvType.CLIENT)
	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		if (entity == null || !(entity instanceof EdenPortalBlockEntity)) {
			return;
		}
		((EdenPortalBlockEntity) entity).ticks++;
	}
	
	@Environment(EnvType.CLIENT)
	public long getTicks() {
		return ticks;
	}
	
	public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		if (entity == null || !(entity instanceof EdenPortalBlockEntity)) {
			return;
		}
		
		EdenPortalBlockEntity portal = (EdenPortalBlockEntity) entity;
		
		if (portal.checkTicks > 10) {
			portal.checkTicks = 0;
			if (!EdenPortal.checkOldPortal(level, blockPos)) {
				EdenPortal.destroyPortal(level, blockPos);
				level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
				portal.setRemoved();
				return;
			}
		}
		portal.checkTicks++;
		
		if (portal.box == null) {
			portal.box = new AABB(blockPos);
			portal.box = portal.box.inflate(0.5, 0, 0.5).setMaxY(blockPos.getY() + 3);
		}
		
		List<Entity> entities = level.getEntities(null, portal.box);
		
		if (entities.isEmpty()) {
			return;
		}
		
		MinecraftServer server = level.getServer();
		ResourceKey key = level.dimension().equals(EdenRing.EDEN_RING_KEY) ? Level.OVERWORLD : EdenRing.EDEN_RING_KEY;
		ServerLevel destination = server.getLevel(key);
		
		if (destination == null) {
			return;
		}
		
		MutableBlockPos preExit = getExit(destination, blockPos);
		if (preExit == null) {
			preExit = blockPos.mutable();
			getLand(destination, preExit);
			if (preExit.getY() == 130 && destination.getBlockState(preExit.below(2)).isAir()) {
				EdenFeatures.SMALL_ISLAND.getFeature().place(new FeaturePlaceContext(
					Optional.empty(),
					destination,
					destination.getChunkSource().getGenerator(),
					destination.random,
					preExit.below(2),
					null
				));
			}
			EdenPortal.buildPortal(destination, preExit);
		}
		
		final MutableBlockPos exit = preExit;
		entities.forEach(e -> {
			if (e.isAlive()) {
				EdenPortable portable = (EdenPortable) e;
				if (portable.getPortalTimeout() > 0) {
					portable.setPortalTimeout(20);
				}
				else {
					if (e instanceof ServerPlayer) {
						ServerPlayer player = (ServerPlayer) e;
						FabricDimensions.teleport(
							player,
							destination,
							new PortalInfo(
								new Vec3(exit.getX() + 0.5, exit.getY(), exit.getZ()+0.5),
								new Vec3(0,0,0),
								player.getYRot(),
								player.getXRot()
							)
						);
						((EdenPortable) player).setPortalTimeout(20);
					}
					else {
						e.ejectPassengers();
						Entity newEntity = e.getType().create(destination);
						newEntity.restoreFrom(e);
						e.remove(RemovalReason.CHANGED_DIMENSION);
						destination.addDuringTeleport(newEntity);
						((EdenPortable) newEntity).setPortalTimeout(100);
						newEntity.teleportTo(exit.getX() + 0.5, exit.getY(), exit.getZ() + 0.5);
					}
				}
			}
		});
	}
	
	private static MutableBlockPos getExit(Level level, BlockPos start) {
		int x1 = (start.getX() >> 4) - 1;
		int z1 = (start.getZ() >> 4) - 1;
		int x2 = x1 + 3;
		int z2 = z1 + 3;
		List<BlockEntity> entityList = new ArrayList<>();
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				Map<BlockPos, BlockEntity> entities = level.getChunk(x, z).getBlockEntities();
				entityList.addAll(entities.values().stream().filter(entity -> entity instanceof EdenPortalBlockEntity).toList());
			}
		}
		final int size = entityList.size();
		if (size == 0) {
			return null;
		}
		BlockEntity target = size == 1 ? entityList.get(0) : entityList.get(level.random.nextInt(size));
		return target.getBlockPos().mutable();
	}
	
	private static void getLand(Level level, MutableBlockPos pos) {
		ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, true);
		pos.setX(pos.getX() & 15);
		pos.setZ(pos.getZ() & 15);
		int maxY = chunk.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()) + 3;
		
		if (maxY < 1) {
			pos.move(chunk.getPos().getMinBlockX(), 2, chunk.getPos().getMinBlockZ()).setY(128 + 2);
			return;
		}
		
		pos.setY(maxY);
		BlockState state = level.getBlockState(pos);
		while (pos.getY() > 0 && (state.isAir() || BlocksHelper.replaceableOrPlant(state))) {
			pos.setY(pos.getY() - 1);
			state = chunk.getBlockState(pos);
		}
		
		if (pos.getY() < 1) {
			pos.setY(128);
		}
		
		pos.move(chunk.getPos().getMinBlockX(), 2, chunk.getPos().getMinBlockZ());
		return;
	}
}
