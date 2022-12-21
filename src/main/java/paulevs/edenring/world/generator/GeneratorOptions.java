package paulevs.edenring.world.generator;

import paulevs.edenring.config.Configs;

public class GeneratorOptions {
	public static LayerOptions bigOptions;
	public static LayerOptions mediumOptions;
	public static LayerOptions smallOptions;
	public static int biomeSizeLand;
	public static int biomeSizeAir;
	public static int biomeSizeCave;
	
	public static void init() {
		var config = Configs.GENERATOR;
		bigOptions = new LayerOptions("terrain.layers.bigIslands", config, 300, 200, 128, 20);
		mediumOptions = new LayerOptions("terrain.layers.mediumIslands", config, 150, 100, 128, 40);
		smallOptions = new LayerOptions("terrain.layers.smallIslands", config, 60, 50, 128, 60);
		biomeSizeLand = config.getInt("biomes", "landBiomeSize", 256);
		biomeSizeAir = config.getInt("biomes", "voidBiomeSize", 256);
		biomeSizeCave = config.getInt("biomes", "caveBiomeSize", 128);
	}
}
