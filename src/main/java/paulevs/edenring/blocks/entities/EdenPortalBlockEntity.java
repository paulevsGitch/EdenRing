package paulevs.edenring.blocks.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenPortable;
import paulevs.edenring.registries.EdenBlockEntities;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.EdenPortal;

import java.util.List;

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
		
		MutableBlockPos exit = getExit(destination, blockPos);
		if (exit.getY() == -255) {
			getLand(destination, exit);
			EdenPortal.buildPortal(destination, exit);
		}
		
		entities.forEach(e -> {
			if (e.isAlive()) {
				EdenPortable portable = (EdenPortable) e;
				if (portable.getPortalTimeout() > 0) {
					portable.setPortalTimeout(20);
				}
				else {
					if (e instanceof ServerPlayer) {
						ServerPlayer player = (ServerPlayer) e;
						player.teleportTo(
							destination,
							exit.getX() + 0.5,
							exit.getY(),
							exit.getZ() + 0.5,
							player.getYRot(),
							player.getXRot()
						);
						((EdenPortable) player).setPortalTimeout(20);
					}
					else {
						Entity newEntity = e.changeDimension(destination);
						if (newEntity != null) {
							newEntity.teleportTo(exit.getX() + 0.5, exit.getY(), exit.getZ() + 0.5);
							((EdenPortable) newEntity).setPortalTimeout(20);
						}
					}
				}
			}
		});
	}
	
	private static MutableBlockPos getExit(Level level, BlockPos start) {
		MutableBlockPos pos = start.mutable();
		ChunkAccess chunk = level.getChunk(start.getX() >> 4, start.getZ() >> 4, ChunkStatus.FULL, true);
		pos.setY(level.getHeight(Types.WORLD_SURFACE, start.getX(), start.getZ()));
		pos.setX(pos.getX() & 15);
		pos.setZ(pos.getZ() & 15);
		int maxY = chunk.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()) + 3;
		for (int y = maxY; y > 0; y--) {
			pos.setY(y);
			if (chunk.getBlockState(pos).is(EdenBlocks.PORTAL_CENTER)) {
				return pos.move(chunk.getPos().getMinBlockX(), 0, chunk.getPos().getMinBlockZ());
			}
		}
		pos.move(chunk.getPos().getMinBlockX(), 0, chunk.getPos().getMinBlockZ()).setY(-255);
		return pos;
	}
	
	private static void getLand(Level level, MutableBlockPos pos) {
		ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, true);
		pos.setX(pos.getX() & 15);
		pos.setZ(pos.getZ() & 15);
		int maxY = chunk.getHeight(Types.WORLD_SURFACE, pos.getX(), pos.getZ()) + 3;
		
		if (maxY < 1) {
			pos.move(chunk.getPos().getMinBlockX(), 2, chunk.getPos().getMinBlockZ()).setY(128);
			return;
		}
		
		pos.setY(maxY);
		BlockState state = level.getBlockState(pos);
		while (pos.getY() > 0 && (state.isAir() || state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT))) {
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
