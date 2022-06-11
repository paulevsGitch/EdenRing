package paulevs.edenring.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.registries.EdenBlocks;

@Mixin(EatBlockGoal.class)
public class EatBlockGoalMixin {
	@Final
	@Shadow
	private Mob mob;
	
	@Final
	@Shadow
	private Level level;
	
	@Inject(method = "canUse()Z", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/entity/Mob;blockPosition()Lnet/minecraft/core/BlockPos;",
		shift = Shift.BEFORE
	), cancellable = true)
	private void eden_canUse(CallbackInfoReturnable<Boolean> info) {
		BlockPos pos = this.mob.blockPosition().below();
		if (level.getBlockState(pos).is(EdenBlocks.EDEN_GRASS_BLOCK)) {
			info.setReturnValue(true);
		}
	}
	
	@Inject(method = "tick()V", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;",
		shift = Shift.AFTER
	), cancellable = true)
	private void eden_tick(CallbackInfo info) {
		BlockPos pos = mob.blockPosition().below();
		if (level.getBlockState(pos).is(EdenBlocks.EDEN_GRASS_BLOCK)) {
			if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
				this.level.levelEvent(2001, pos, Block.getId(EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState()));
				this.level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
			}
			this.mob.ate();
		}
	}
}
