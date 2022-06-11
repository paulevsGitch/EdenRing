package paulevs.edenring.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.betterx.bclib.interfaces.ItemModelProvider;
import paulevs.edenring.entities.EdenPainting;

public class EdenPaintingItem extends HangingEntityItem implements ItemModelProvider {
	public EdenPaintingItem(EntityType<? extends HangingEntity> entityType, Properties properties) {
		super(entityType, properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext useOnContext) {
		BlockPos clickedPos = useOnContext.getClickedPos();
		Direction direction = useOnContext.getClickedFace();
		BlockPos relativePos = clickedPos.relative(direction);
		Player player = useOnContext.getPlayer();
		ItemStack itemStack = useOnContext.getItemInHand();
		
		if (player != null && !this.mayPlace(player, direction, itemStack, relativePos)) {
			return InteractionResult.FAIL;
		}
		
		Level level = useOnContext.getLevel();
		HangingEntity hangingEntity = new EdenPainting(level, relativePos, direction);
		CompoundTag compoundTag = itemStack.getTag();
		
		if (compoundTag != null) {
			EntityType.updateCustomEntityTag(level, player, hangingEntity, compoundTag);
		}
		
		if (hangingEntity.survives()) {
			if (!level.isClientSide) {
				hangingEntity.playPlacementSound();
				level.gameEvent(player, GameEvent.ENTITY_PLACE, clickedPos);
				level.addFreshEntity(hangingEntity);
			}
			itemStack.shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		
		return InteractionResult.CONSUME;
	}
}
