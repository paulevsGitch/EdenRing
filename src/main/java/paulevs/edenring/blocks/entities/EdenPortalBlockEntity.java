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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import paulevs.edenring.EdenRing;
import paulevs.edenring.interfaces.EdenPortable;
import paulevs.edenring.registries.EdenBlockEntities;
import paulevs.edenring.registries.EdenBlocks;

import java.util.List;

public class EdenPortalBlockEntity extends BlockEntity {
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
			int posY = destination.getHeight(Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ());
			exit.setY(posY);
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
		pos.setY(level.getHeight(Types.WORLD_SURFACE, start.getX(), start.getZ()));
		int maxY = level.getHeight(Types.WORLD_SURFACE, start.getX(), start.getZ()) + 10;
		for (int y = maxY; y > 0; y--) {
			pos.setY(y);
			if (level.getBlockState(pos).is(EdenBlocks.PORTAL_CENTER)) {
				return pos;
			}
		}
		pos.setY(-255);
		return pos;
	}
}
