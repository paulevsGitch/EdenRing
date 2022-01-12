package paulevs.edenring.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import paulevs.edenring.gui.GuideBookScreen;
import ru.bclib.BCLib;
import ru.bclib.items.ModelProviderItem;

public class GuideBookItem extends ModelProviderItem {
	public GuideBookItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		if (BCLib.isClient() && level.isClientSide()) {
			openClientScreen();
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}
	
	@Environment(EnvType.CLIENT)
	private void openClientScreen() {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.setScreen(new GuideBookScreen());
	}
}
