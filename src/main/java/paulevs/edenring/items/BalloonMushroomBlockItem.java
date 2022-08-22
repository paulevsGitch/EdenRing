package paulevs.edenring.items;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
		
		if (hit != null) {
			ItemStack stack = player.getItemInHand(interactionHand);
			this.place(new BlockPlaceContext(player, interactionHand, stack, hit));
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}
		
		return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
	}
}
