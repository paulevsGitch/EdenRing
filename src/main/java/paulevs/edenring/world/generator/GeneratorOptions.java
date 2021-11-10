package paulevs.edenring.world.generator;

import paulevs.edenring.EdenRing;
import ru.bclib.config.PathConfig;

public class GeneratorOptions {
	public static LayerOptions bigOptions;
	public static LayerOptions mediumOptions;
	public static LayerOptions smallOptions;
	public static int biomeSize;
	
	public static void init() {
		PathConfig config = new PathConfig(EdenRing.MOD_ID, "generator", false, false);
		
		bigOptions = new LayerOptions("terrain.layers.bigIslands", config, 300, 200, 128, 20);
		mediumOptions = new LayerOptions("terrain.layers.mediumIslands", config, 150, 100, 128, 40);
		smallOptions = new LayerOptions("terrain.layers.smallIslands", config, 60, 50, 128, 60);
		biomeSize = config.getInt("biomes", "size", 128);
		
		config.saveChanges();
	}
}
