package paulevs.edenring.registries;


import paulevs.edenring.EdenRing;
import ru.bclib.config.PathConfig;
import ru.bclib.registry.ItemRegistry;

public class EdenItems {
	public static final ItemRegistry REGISTRY = new ItemRegistry(EdenRing.EDEN_TAB, new PathConfig(EdenRing.MOD_ID, "items"));
	
	public static void init() {}
}
