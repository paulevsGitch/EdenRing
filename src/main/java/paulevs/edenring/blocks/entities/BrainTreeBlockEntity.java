package paulevs.edenring.blocks.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity.AnimationStatus;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenBlockEntities;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.util.MHelper;

public class BrainTreeBlockEntity extends BlockEntity {
	private boolean active = false;
	
	@Environment(EnvType.CLIENT)
	private long animation;
	
	@Environment(EnvType.CLIENT)
	private AnimationStatus status = AnimationStatus.CLOSED;
	
	public BrainTreeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(EdenBlockEntities.BRAIN_BLOCK, blockPos, blockState);
		animation = MHelper.RANDOM.nextInt(256);
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putBoolean("isActive", active);
		return super.save(tag);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		animation = MHelper.RANDOM.nextInt(256);
		if (tag.contains("isActive")) {
			active = tag.getBoolean("isActive");
		}
		status = active ? AnimationStatus.OPENED : AnimationStatus.CLOSED;
	}
	
	@Environment(EnvType.CLIENT)
	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		BrainTreeBlockEntity brain = (BrainTreeBlockEntity) entity;
		brain.active = state.getValue(BlockProperties.ACTIVE);
		brain.animation++;
		
		if (brain.active) {
			if (brain.status == AnimationStatus.CLOSED) {
				brain.status = AnimationStatus.OPENING;
			}
			else if (brain.status == AnimationStatus.OPENING) {
				brain.status = AnimationStatus.OPENED;
			}
		}
		else {
			if (brain.status == AnimationStatus.OPENED) {
				brain.status = AnimationStatus.CLOSING;
			}
			else if (brain.status == AnimationStatus.CLOSING) {
				brain.status = AnimationStatus.CLOSED;
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public long getAnimation() {
		return animation;
	}
	
	@Environment(EnvType.CLIENT)
	public AnimationStatus getStatus() {
		return status;
	}
}
