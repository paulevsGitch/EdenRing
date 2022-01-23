package paulevs.edenring.world.generator.terrain;

public class TerrainManager {
	//private static Map<ChunkPos, >
	private static Thread generationThread;
	private static boolean active;
	
	public static void start() {
		active = true;
		if (generationThread == null || !generationThread.isAlive()) {
			generationThread = new Thread(() -> {
				while (active) {
					process();
				}
			});
			generationThread.start();
		}
	}
	
	private static void process() {
	
	}
	
	private class DataCache {
		double[][] data;
	}
}
