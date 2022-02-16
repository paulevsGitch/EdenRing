package paulevs.edenring.world.generator;

import com.google.common.collect.Maps;
import net.minecraft.world.level.biome.Climate.Sampler;

import java.util.Map;

public class MultiThreadGenerator {
	private static final Map<Long, TerrainGenerator> TERRAIN_POOL = Maps.newConcurrentMap();
	private static final Map<Long, TerrainGenerator> BIOME_POOL = Maps.newConcurrentMap();
	private static Sampler sampler;
	private static long seed;
	
	public static void init(long seed, Sampler sampler) {
		MultiThreadGenerator.sampler = sampler;
		MultiThreadGenerator.seed = seed;
		TERRAIN_POOL.clear();
		BIOME_POOL.clear();
	}
	
	public static TerrainGenerator getTerrainGenerator() {
		return TERRAIN_POOL.computeIfAbsent(Thread.currentThread().getId(), i -> new TerrainGenerator(seed));
	}
	
	public static TerrainGenerator getBiomeGenerator() {
		return BIOME_POOL.computeIfAbsent(Thread.currentThread().getId(), i -> new TerrainGenerator(seed));
	}
	
	public static long getSeed() {
		return seed;
	}
	
	public static Sampler getSampler() {
		return sampler;
	}
}
