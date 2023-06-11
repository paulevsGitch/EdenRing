package paulevs.edenring.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.betterx.bclib.entity.DespawnableAnimal;
import org.betterx.bclib.util.MHelper;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.registries.EdenSounds;

import java.util.EnumSet;

public class DiskwingEntity extends DespawnableAnimal {
	private static final EntityDataAccessor<Byte> VARIANT = SynchedEntityData.defineId(DiskwingEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Byte> SIZE = SynchedEntityData.defineId(DiskwingEntity.class, EntityDataSerializers.BYTE);
	
	private Vec3 moveTargetPoint = Vec3.ZERO;
	private BlockPos anchorPoint = BlockPos.ZERO;
	
	public DiskwingEntity(EntityType<DiskwingEntity> entityType, Level level) {
		super(entityType, level);
		this.moveControl = new DiskwingMoveControl(this);
		this.lookControl = new DiskwingLookControl(this);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new ChangeAnchoePointGoal());
		this.goalSelector.addGoal(1, new DiskwingCircleAroundAnchorGoal());
	}
	
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, SpawnGroupData entityData, CompoundTag entityTag) {
		SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
		
		this.entityData.set(VARIANT, (byte) world.getLevel().random.nextInt(DiskwingType.VALUES.length));
		this.entityData.set(SIZE, (byte) world.getLevel().random.nextInt(255));
		
		if (entityTag != null) {
			if (entityTag.contains("Variant")) {
				byte variant = entityTag.getByte("Variant");
				if (variant >= DiskwingType.VALUES.length) {
					variant = 0;
				}
				this.entityData.set(VARIANT, variant);
			}
			if (entityTag.contains("Size")) {
				byte size = entityTag.getByte("Size");
				this.entityData.set(SIZE, size);
			}
		}
		
		anchorPoint = this.blockPosition().above(5);
		
		this.refreshDimensions();
		return data;
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(VARIANT, (byte) 0);
		this.entityData.define(SIZE, (byte) 0);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("Variant", this.entityData.get(VARIANT));
		tag.putByte("Size", this.entityData.get(SIZE));
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
		if (tag.contains("Size")) {
			byte variant = tag.getByte("Size");
			this.entityData.set(SIZE, variant);
		}
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		DiskwingEntity entity = EdenEntities.DISKWING.create(serverLevel);
		entity.setVariant(DiskwingEntity.class.cast(ageableMob).getVariant());
		entity.setScale((byte) ageableMob.getRandom().nextInt(255));
		return entity;
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
	
	@Override
	protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {}
	
	@Override
	public boolean onClimbable() {
		return false;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return EdenSounds.DISKWING_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return EdenSounds.DISKWING_DAMAGE;
	}
	
	@Override
	protected void playStepSound(BlockPos blockPos, BlockState blockState) {}
	
	public DiskwingType getVariant() {
		byte type = this.entityData.get(VARIANT);
		return DiskwingType.VALUES[type];
	}
	
	public void setVariant(DiskwingType variant) {
		this.entityData.set(VARIANT, (byte) variant.ordinal());
	}
	
	@Override
	public float getScale() {
		short size = (short) (this.entityData.get(SIZE) & 255);
		float scale = this.isBaby() ? 0.5F : 1.0F;
		return scale * Mth.lerp(size / 255F, 0.75F, 1.0F);
	}
	
	public void setScale(byte scale) {
		this.entityData.set(SIZE, scale);
	}
	
	@Override
	protected BodyRotationControl createBodyControl() {
		return new DiskwingBodyRotationControl(this);
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
	
	class DiskwingMoveControl extends MoveControl {
		private float speed;
		
		public DiskwingMoveControl(Mob mob) {
			super(mob);
			this.speed = 0.1f;
		}
		
		@Override
		public void tick() {
			if (DiskwingEntity.this.horizontalCollision) {
				DiskwingEntity.this.setYRot(DiskwingEntity.this.getYRot() + 180.0f);
				this.speed = 0.1f;
			}
			
			float f = (float) (DiskwingEntity.this.moveTargetPoint.x - DiskwingEntity.this.getX());
			float g = (float) (DiskwingEntity.this.moveTargetPoint.y - DiskwingEntity.this.getY());
			float h = (float) (DiskwingEntity.this.moveTargetPoint.z - DiskwingEntity.this.getZ());
			
			float d = MHelper.length(f, h);
			if (Math.abs(d) > 1.0E-5F) {
				float e = 1.0F - Mth.abs(g * 0.7F) / d;
				f *= e;
				h *= e;
				d = MHelper.length(f, h);
				double i = Mth.sqrt(f * f + h * h + g * g);
				float j = DiskwingEntity.this.getYRot();
				float k = (float) Mth.atan2(h, f);
				float l = Mth.wrapDegrees(DiskwingEntity.this.getYRot() + 90.0f);
				float m = Mth.wrapDegrees(k * 57.295776f);
				DiskwingEntity.this.setYRot(Mth.approachDegrees(l, m, 4.0f) - 90.0f);
				DiskwingEntity.this.yBodyRot = DiskwingEntity.this.getYRot();
				this.speed = Mth.degreesDifferenceAbs(j, DiskwingEntity.this.getYRot()) < 3.0f ? Mth.approach(this.speed, 1.8f, 0.005f * (1.8f / this.speed)) : Mth.approach(this.speed, 0.2f, 0.025f);
				float n = (float)(-(Mth.atan2(-g, d) * 57.2957763671875));
				DiskwingEntity.this.setXRot(n);
				float o = DiskwingEntity.this.getYRot() + 90.0f;
				double p = (this.speed * Mth.cos(o * ((float) Math.PI / 180))) * Math.abs(f / i);
				double q = (this.speed * Mth.sin(o * ((float) Math.PI / 180))) * Math.abs(h / i);
				double r = (this.speed * Mth.sin(n * ((float) Math.PI / 180))) * Math.abs(g / i);
				Vec3 vec3 = DiskwingEntity.this.getDeltaMovement();
				DiskwingEntity.this.setDeltaMovement(vec3.add(new Vec3(p, r, q).subtract(vec3).scale(0.2)));
			}
		}
	}
	
	class DiskwingLookControl extends LookControl {
		public DiskwingLookControl(Mob mob) {
			super(mob);
		}
		
		@Override
		public void tick() {
		}
	}
	
	class DiskwingBodyRotationControl extends BodyRotationControl {
		public DiskwingBodyRotationControl(Mob mob) {
			super(mob);
		}
		
		@Override
		public void clientTick() {
			DiskwingEntity.this.yHeadRot = DiskwingEntity.this.yBodyRot;
			DiskwingEntity.this.yBodyRot = DiskwingEntity.this.getYRot();
		}
	}
	
	class ChangeAnchoePointGoal extends Goal {
		@Override
		public boolean canUse() {
			return DiskwingEntity.this.random.nextInt(32) == 0;
		}
		
		@Override
		public void start() {
			RandomSource random = DiskwingEntity.this.random;
			BlockPos point = DiskwingEntity.this.anchorPoint;
			Level level = DiskwingEntity.this.level();
			int x = point.getX() + MHelper.randRange(-20, 20, random);
			int y = point.getY() + MHelper.randRange(-20, 20, random);
			int z = point.getZ() + MHelper.randRange(-20, 20, random);
			if (point.getY() - 64 < level.getMinBuildHeight()) {
				y = level.getMinBuildHeight() + 64;
			}
			else if (point.getY() + 64 > level.getMaxBuildHeight()) {
				y = level.getMaxBuildHeight() - 64;
			}
			point = new BlockPos(x, y, z);
			if (level.getBlockState(point).isAir()) {
				DiskwingEntity.this.anchorPoint = point;
			}
		}
	}
	
	class DiskwingCircleAroundAnchorGoal extends Goal {
		private float angle;
		private float distance;
		private float height;
		private float clockwise;
		
		DiskwingCircleAroundAnchorGoal() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		protected boolean touchingTarget() {
			return DiskwingEntity.this.moveTargetPoint.distanceToSqr(DiskwingEntity.this.getX(), DiskwingEntity.this.getY(), DiskwingEntity.this.getZ()) < 4.0;
		}
		
		@Override
		public boolean canUse() {
			return true;
		}
		
		@Override
		public void start() {
			this.distance = 5.0f + DiskwingEntity.this.random.nextFloat() * 10.0f;
			this.height = -4.0f + DiskwingEntity.this.random.nextFloat() * 9.0f;
			this.clockwise = DiskwingEntity.this.random.nextBoolean() ? 1.0f : -1.0f;
			this.selectNext();
		}
		
		@Override
		public void tick() {
			if (DiskwingEntity.this.random.nextInt(this.adjustedTickDelay(350)) == 0) {
				this.height = -4.0f + DiskwingEntity.this.random.nextFloat() * 9.0f;
			}
			if (DiskwingEntity.this.random.nextInt(this.adjustedTickDelay(250)) == 0) {
				this.distance += 1.0f;
				if (this.distance > 15.0f) {
					this.distance = 5.0f;
					this.clockwise = -this.clockwise;
				}
			}
			if (DiskwingEntity.this.random.nextInt(this.adjustedTickDelay(450)) == 0) {
				this.angle = DiskwingEntity.this.random.nextFloat() * 2.0f * (float)Math.PI;
				this.selectNext();
			}
			if (this.touchingTarget()) {
				this.selectNext();
			}
			if (DiskwingEntity.this.moveTargetPoint.y < DiskwingEntity.this.getY() && !DiskwingEntity.this.level().isEmptyBlock(DiskwingEntity.this.blockPosition().below(1))) {
				this.height = Math.max(1.0f, this.height);
				this.selectNext();
			}
			if (DiskwingEntity.this.moveTargetPoint.y > DiskwingEntity.this.getY() && !DiskwingEntity.this.level().isEmptyBlock(DiskwingEntity.this.blockPosition().above(1))) {
				this.height = Math.min(-1.0f, this.height);
				this.selectNext();
			}
		}
		
		private void selectNext() {
			if (BlockPos.ZERO.equals(DiskwingEntity.this.anchorPoint)) {
				DiskwingEntity.this.anchorPoint = DiskwingEntity.this.blockPosition();
			}
			this.angle += this.clockwise * 15.0f * ((float)Math.PI / 180);
			DiskwingEntity.this.moveTargetPoint = Vec3.atLowerCornerOf(DiskwingEntity.this.anchorPoint).add(this.distance * Mth.cos(this.angle), -4.0f + this.height, this.distance * Mth.sin(this.angle));
		}
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double d) {
		return super.shouldRenderAtSqrDistance(d * 0.1);
	}
}
