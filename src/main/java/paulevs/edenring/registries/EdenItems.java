package paulevs.edenring.registries;


import net.minecraft.world.item.Item;
import paulevs.edenring.EdenRing;
import paulevs.edenring.items.GuideBookItem;
import ru.bclib.config.PathConfig;
import ru.bclib.registry.ItemRegistry;

public class EdenItems {
	private static final PathConfig ITEMS_CONFIG = new PathConfig(EdenRing.MOD_ID, "items");
	public static final ItemRegistry REGISTRY = new ItemRegistry(EdenRing.EDEN_TAB, ITEMS_CONFIG);
	
	public static final Item GUIDE_BOOK = register("guide_book", new GuideBookItem(REGISTRY.makeItemSettings()));
	
	public static void init() {
		ITEMS_CONFIG.saveChanges();
	}
	
	private static Item register(String name, Item item) {
		REGISTRY.register(EdenRing.makeID(name), item);
		return item;
	}
}
