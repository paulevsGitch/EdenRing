package paulevs.edenring.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LightningRayEntity extends Entity {
	private static final EntityDataAccessor<Float> DATA_DIR_X_ID = SynchedEntityData.defineId(LightningRayEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_DIR_Y_ID = SynchedEntityData.defineId(LightningRayEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> DATA_DIR_Z_ID = SynchedEntityData.defineId(LightningRayEntity.class, EntityDataSerializers.FLOAT);
	private static final int MAX_AGE = 8;
	
	private Vec3 direction;
	private int age;
	
	public LightningRayEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.age = MAX_AGE;
	}
	
	public void setEnd(Vec3 end) {
		direction = end.subtract(position());
		entityData.set(DATA_DIR_X_ID, (float) direction.x);
		entityData.set(DATA_DIR_Y_ID, (float) direction.y);
		entityData.set(DATA_DIR_Z_ID, (float) direction.z);
	}
	
	public void setDir(Vec3 direction) {
		this.direction = direction;
		entityData.set(DATA_DIR_X_ID, (float) direction.x);
		entityData.set(DATA_DIR_Y_ID, (float) direction.y);
		entityData.set(DATA_DIR_Z_ID, (float) direction.z);
	}
	
	public Vec3 getDir() {
		if (direction == null) {
			float x = this.entityData.get(DATA_DIR_X_ID);
			float y = this.entityData.get(DATA_DIR_Y_ID);
			float z = this.entityData.get(DATA_DIR_Z_ID);
			direction = new Vec3(x, y, z);
		}
		return direction;
	}
	
	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA_DIR_X_ID, 0F);
		this.entityData.define(DATA_DIR_Y_ID, 0F);
		this.entityData.define(DATA_DIR_Z_ID, 0F);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("direction")) {
			setDir(vecFromNBT((ListTag) tag.get("direction")));
		}
		
		if (tag.contains("age")) {
			age = tag.getInt("age");
		}
		else {
			age = MAX_AGE;
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.put("direction", vecToNBT(getDir()));
		tag.putInt("age", age);
	}
	
	@Override
	@NotNull
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
	@Override
	public void tick() {
		super.tick();
		age--;
		if (age < 1) {
			this.remove(RemovalReason.DISCARDED);
		}
	}
	
	/*@Override
	public float getBrightness() {
		return 1.0F;
	}*/
	
	private ListTag vecToNBT(Vec3 vec) {
		ListTag tag = new ListTag();
		tag.add(DoubleTag.valueOf(vec.x));
		tag.add(DoubleTag.valueOf(vec.y));
		tag.add(DoubleTag.valueOf(vec.z));
		return tag;
	}
	
	private Vec3 vecFromNBT(ListTag tag) {
		double x = tag.getDouble(0);
		double y = tag.getDouble(1);
		double z = tag.getDouble(2);
		return new Vec3(x, y, z);
	}
}
