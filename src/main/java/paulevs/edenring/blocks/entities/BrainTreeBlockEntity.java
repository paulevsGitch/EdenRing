package paulevs.edenring.blocks.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenBlockEntities;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.util.MHelper;

public class BrainTreeBlockEntity extends BlockEntity {
	private boolean active = false;
	
	@Environment(EnvType.CLIENT)
	private long animation;
	
	@Environment(EnvType.CLIENT)
	private int status;
	
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
		status = active ? 5 : 0;
	}
	
	@Environment(EnvType.CLIENT)
	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		BrainTreeBlockEntity brain = (BrainTreeBlockEntity) entity;
		boolean activate = state.getValue(BlockProperties.ACTIVE);
		
		if (activate != brain.active) {
			brain.status = 0;
		}
		else if (brain.status < 5) {
			brain.status++;
		}
		
		brain.active = activate;
		brain.animation++;
	}
	
	@Environment(EnvType.CLIENT)
	public long getAnimation() {
		return animation;
	}
	
	@Environment(EnvType.CLIENT)
	public int getStatus() {
		return status;
	}
}
