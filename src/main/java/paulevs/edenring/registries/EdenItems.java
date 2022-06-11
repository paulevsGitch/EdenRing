package paulevs.edenring.registries;


import net.minecraft.world.item.Item;
import org.betterx.bclib.api.v2.ComposterAPI;
import org.betterx.bclib.config.PathConfig;
import org.betterx.bclib.items.ModelProviderItem;
import org.betterx.bclib.registry.ItemRegistry;
import paulevs.edenring.EdenRing;
import paulevs.edenring.items.EdenPaintingItem;
import paulevs.edenring.items.GuideBookItem;

public class EdenItems {
	private static final PathConfig ITEMS_CONFIG = new PathConfig(EdenRing.MOD_ID, "items");
	public static final ItemRegistry REGISTRY = new ItemRegistry(EdenRing.EDEN_TAB, ITEMS_CONFIG);
	
	public static final Item GUIDE_BOOK = register("guide_book", new GuideBookItem(REGISTRY.makeItemSettings()));
	public static final Item LIMPHIUM_LEAF = register("limphium_leaf", new ModelProviderItem(REGISTRY.makeItemSettings()));
	public static final Item LIMPHIUM_LEAF_DRYED = register("limphium_leaf_dryed", new ModelProviderItem(REGISTRY.makeItemSettings()));
	public static final Item LIMPHIUM_PAINTING = register("limphium_painting", new EdenPaintingItem(EdenEntities.LIMPHIUM_PAINTING, REGISTRY.makeItemSettings()));
	
	public static void init() {
		ITEMS_CONFIG.saveChanges();
		ComposterAPI.allowCompost(0.3F, LIMPHIUM_LEAF);
		ComposterAPI.allowCompost(0.3F, LIMPHIUM_LEAF_DRYED);
	}
	
	private static Item register(String name, Item item) {
		REGISTRY.register(EdenRing.makeID(name), item);
		return item;
	}
}
