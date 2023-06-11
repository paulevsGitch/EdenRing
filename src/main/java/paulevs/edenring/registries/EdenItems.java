package paulevs.edenring.registries;


import net.minecraft.world.item.Item;
import org.betterx.bclib.api.v2.ComposterAPI;
import org.betterx.bclib.items.ModelProviderItem;
import org.betterx.bclib.registry.ItemRegistry;
import paulevs.edenring.EdenRing;
import paulevs.edenring.config.Configs;
import paulevs.edenring.items.EdenPaintingItem;
import paulevs.edenring.items.GuideBookItem;

public class EdenItems {
	public static final ItemRegistry REGISTRY = new ItemRegistry(Configs.ITEMS);
	
	public static final Item GUIDE_BOOK = register("guide_book", new GuideBookItem(REGISTRY.makeItemSettings().stacksTo(16)));
	public static final Item LIMPHIUM_LEAF = register("limphium_leaf", new ModelProviderItem(REGISTRY.makeItemSettings()));
	public static final Item LIMPHIUM_LEAF_DRYED = register("limphium_leaf_dryed", new ModelProviderItem(REGISTRY.makeItemSettings()));
	public static final Item LIMPHIUM_PAINTING = register("limphium_painting", new EdenPaintingItem(EdenEntities.LIMPHIUM_PAINTING, REGISTRY.makeItemSettings()));
	
	public static void init() {
		ComposterAPI.allowCompost(0.3F, LIMPHIUM_LEAF);
		ComposterAPI.allowCompost(0.3F, LIMPHIUM_LEAF_DRYED);
	}
	
	private static Item register(String name, Item item) {
		REGISTRY.register(EdenRing.makeID(name), item);
		return item;
	}
}
