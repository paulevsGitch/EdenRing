package paulevs.edenring.registries;

import paulevs.edenring.EdenRing;
import ru.bclib.config.PathConfig;

public class EdenRecipes {
	public static final PathConfig CONFIG = new PathConfig(EdenRing.MOD_ID, "recipes");
	
	public static void init() {
		CONFIG.saveChanges();
	}
}
