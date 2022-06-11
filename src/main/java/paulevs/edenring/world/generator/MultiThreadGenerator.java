package paulevs.edenring.world.generator;

import com.google.common.collect.Maps;

import java.util.Map;

public class MultiThreadGenerator {
	private static final Map<Long, TerrainGenerator> TERRAIN_POOL = Maps.newConcurrentMap();
	private static long seed;
	
	public static void init(long seed) {
		MultiThreadGenerator.seed = seed;
		TERRAIN_POOL.clear();
	}
	
	public static TerrainGenerator getTerrainGenerator() {
		return TERRAIN_POOL.computeIfAbsent(Thread.currentThread().getId(), i -> new TerrainGenerator(seed));
	}
	
	public static long getSeed() {
		return seed;
	}
}
