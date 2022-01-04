package paulevs.edenring.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BalloonMushroomBlockItem extends BlockItem {
	public BalloonMushroomBlockItem(Block block, Properties properties) {
		super(block, properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		Vec3 vec = new Vec3(0, 0, 1).xRot(-player.getXRot() * Mth.DEG_TO_RAD).yRot(-player.getYHeadRot() * Mth.DEG_TO_RAD);
		BlockHitResult hit = level.isBlockInLine(new ClipBlockStateContext(
			player.getEyePosition(),
			player.getEyePosition().add(vec.scale(4.9)),
			state -> state.isAir()
		));
		
		if (hit != null && hit instanceof BlockHitResult) {
			BlockPos pos = hit.getBlockPos();
			BlockState state = getBlock().defaultBlockState();
			SoundType soundType = state.getSoundType();
			level.setBlock(pos, state, 0);
			level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
			level.playSound(player, pos, this.getPlaceSound(state), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
			if (!player.getAbilities().instabuild && level.getBlockState(pos).equals(state)) {
				player.getItemInHand(interactionHand).shrink(1);
			}
			return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
		}
		
		return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
	}
}
