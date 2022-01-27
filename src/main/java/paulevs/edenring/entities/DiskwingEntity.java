package paulevs.edenring.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenEntities;
import ru.bclib.entity.DespawnableAnimal;

public class DiskwingEntity extends DespawnableAnimal {
	private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.defineId(DiskwingEntity.class, EntityDataSerializers.BYTE);
	
	public DiskwingEntity(EntityType<DiskwingEntity> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityTag) {
		SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
		
		this.entityData.set(VARIANT, (byte) world.getLevel().random.nextInt(DiskwingType.VALUES.length));
		
		if (entityTag != null) {
			if (entityTag.contains("Variant")) {
				byte variant = entityTag.getByte("Variant");
				if (variant >= DiskwingType.VALUES.length) {
					variant = 0;
				}
				this.entityData.set(VARIANT, variant);
			}
		}
		
		this.refreshDimensions();
		return data;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, (byte) 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("Variant", this.entityData.get(VARIANT));
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Variant")) {
			byte variant = tag.getByte("Variant");
			if (variant >= DiskwingType.VALUES.length) {
				variant = 0;
			}
			this.entityData.set(VARIANT, variant);
		}
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return EdenEntities.DISKWING.create(serverLevel);
	}
	
	@Override
	public boolean canBeLeashed(Player player) {
		return false;
	}
	
	@Override
	public boolean isPushable() {
		return false;
	}
	
	@Override
	public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	public boolean isNoGravity() {
		return true;
	}
	
	public DiskwingType getVariant() {
		byte type = this.entityData.get(VARIANT);
		return DiskwingType.VALUES[type];
	}
	
	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity
			.createLivingAttributes()
			.add(Attributes.MAX_HEALTH, 8.0D)
			.add(Attributes.FOLLOW_RANGE, 16.0D)
			.add(Attributes.FLYING_SPEED, 1.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.1D);
	}
	
	public enum DiskwingType {
		BLUE("blue"),
		GREEN("green"),
		RED("red"),
		PURPLE("purple");
		
		public static final DiskwingType[] VALUES = DiskwingType.values();
		private final ResourceLocation texture;
		private final ResourceLocation glow;
		
		DiskwingType(String texture) {
			this.texture = EdenRing.makeID("textures/entity/diskwing/diskwing_" + texture + ".png");
			this.glow = EdenRing.makeID("textures/entity/diskwing/diskwing_" + texture + "_glow.png");
		}
		
		public ResourceLocation getTexture() {
			return texture;
		}
		
		public ResourceLocation getGlow() {
			return glow;
		}
	}
}
