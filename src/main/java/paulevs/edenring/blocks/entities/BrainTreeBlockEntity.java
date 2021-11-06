package paulevs.edenring.blocks.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenBlockEntities;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.util.MHelper;

public class BrainTreeBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	private boolean active = false;
	private int ticks;
	
	@Environment(EnvType.CLIENT)
	private long animation;
	
	@Environment(EnvType.CLIENT)
	private int status;
	
	public BrainTreeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(EdenBlockEntities.BRAIN_BLOCK, blockPos, blockState);
		animation = MHelper.RANDOM.nextInt(256);
		ticks = MHelper.randRange(100, 200, MHelper.RANDOM);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.putBoolean("isActive", active);
		tag.putInt("ticks", ticks);
		return super.save(tag);
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		animation = MHelper.RANDOM.nextInt(256);
		if (tag.contains("isActive")) {
			active = tag.getBoolean("isActive");
		}
		if (tag.contains("ticks")) {
			ticks = tag.getInt("ticks");
		}
		status = active ? 5 : 0;
	}
	
	@Environment(EnvType.CLIENT)
	public static <T extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		if (!(entity instanceof BrainTreeBlockEntity)) {
			return;
		}
		
		BrainTreeBlockEntity brain = (BrainTreeBlockEntity) entity;
		
		if (state.getValue(BlockProperties.ACTIVE) != brain.active) {
			brain.active = !brain.active;
			brain.status = 0;
		}
		
		brain.animation++;
		if (brain.status < 5) {
			brain.status++;
		}
	}
	
	@Environment(EnvType.CLIENT)
	public long getAnimation() {
		return animation;
	}
	
	@Environment(EnvType.CLIENT)
	public int getStatus() {
		return status;
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		this.load(tag);
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return this.save(tag);
	}
	
	public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState state, T entity) {
		if (!(entity instanceof BrainTreeBlockEntity)) {
			return;
		}
		
		BrainTreeBlockEntity brain = (BrainTreeBlockEntity) entity;
		if (brain.ticks == 0) {
			boolean stateActive = !state.getValue(BlockProperties.ACTIVE);
			//level.setBlockAndUpdate(blockPos, state.setValue(BlockProperties.ACTIVE, stateActive));
			brain.ticks = stateActive ? MHelper.randRange(40, 80, level.random) : MHelper.randRange(100, 200, level.random);
			brain.active = stateActive;
			brain.status = 0;
		}
		brain.ticks--;
	}
}
