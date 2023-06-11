package paulevs.edenring.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.paintings.EdenPaintings;
import paulevs.edenring.paintings.PaintingInfo;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.registries.EdenItems;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EdenPainting extends HangingEntity {
	private static final EntityDataAccessor<Integer> DATA_RAW_ID = SynchedEntityData.defineId(EdenPainting.class, EntityDataSerializers.INT);
	private PaintingInfo info;
	
	public EdenPainting(EntityType<EdenPainting> entityType, Level level) {
		super(entityType, level);
	}
	
	public EdenPainting(Level level, BlockPos blockPos, Direction direction) {
		this(EdenEntities.LIMPHIUM_PAINTING, level);
		this.pos = blockPos;
		
		AtomicInteger cells = new AtomicInteger();
		List<PaintingInfo> paintings = EdenPaintings.getPaintings().stream().filter(info -> {
			this.info = info;
			this.setDirection(direction);
			int c = info.getCells();
			if (this.survives()) {
				if (c > cells.get()) cells.set(c);
				return true;
			}
			return false;
		}).toList();
		
		paintings = paintings.stream().filter(info -> info.getCells() >= cells.get()).toList();
		
		if (!paintings.isEmpty()) {
			this.info = paintings.get(random.nextInt(paintings.size()));
		}
		
		this.setDirection(direction);
		this.updateData();
	}
	
	public EdenPainting(Level level, BlockPos pos, Direction direction, PaintingInfo info) {
		this(EdenEntities.LIMPHIUM_PAINTING, level);
		this.setDirection(direction);
		this.info = info;
		this.pos = pos;
		this.updateData();
	}
	
	public PaintingInfo getPainting() {
		return info;
	}
	
	private void updateData() {
		this.getEntityData().set(DATA_RAW_ID, info.getRawID());
	}
	
	@Override
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	
	@Override
	public void moveTo(double d, double e, double f, float g, float h) {
		this.setPos(d, e, f);
	}
	
	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(DATA_RAW_ID, 0);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		super.onSyncedDataUpdated(entityDataAccessor);
		info = EdenPaintings.getPainting(this.getEntityData().get(DATA_RAW_ID));
		this.recalculateBoundingBox();
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putString("PaintingInfo", info.getId().toString());
		compoundTag.putByte("Facing", (byte) this.direction.get2DDataValue());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.info = EdenPaintings.getPainting(ResourceLocation.tryParse(compoundTag.getString("PaintingInfo")));
		this.setDirection(Direction.from2DDataValue(compoundTag.getByte("Facing")));
		this.updateData();
	}
	
	@Override
	public void dropItem(@Nullable Entity entity) {
		if (!this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
			return;
		}
		this.playSound(SoundEvents.PAINTING_BREAK, 1.0f, 1.0f);
		if (entity instanceof Player) {
			Player player = (Player)entity;
			if (player.getAbilities().instabuild) {
				return;
			}
		}
		this.spawnAtLocation(EdenItems.LIMPHIUM_PAINTING);
	}
	
	@Override
	public ItemStack getPickResult() {
		return new ItemStack(EdenItems.LIMPHIUM_PAINTING);
	}
	
	@Override
	public void playPlacementSound() {
		this.playSound(SoundEvents.PAINTING_PLACE, 1.0f, 1.0f);
	}
	
	@Override
	public int getWidth() {
		return info == null ? 16 : info.getWidth();
	}
	
	@Override
	public int getHeight() {
		return info == null ? 16 : info.getHeight();
	}
	
	@Override
	@NotNull
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		//return new ClientboundAddEntityPacket(this, this.getType(), this.direction.get2DDataValue(), this.pos);
		return new ClientboundAddEntityPacket(this);
	}
	
	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.setDirection(Direction.from2DDataValue(packet.getData()));
	}
}
