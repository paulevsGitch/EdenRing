package paulevs.edenring.world.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TerrainGenerator {
	private static final Map<Point, TerrainBoolCache> TERRAIN_BOOL_CACHE_MAP = Maps.newHashMap();
	private static final ReentrantLock LOCKER = new ReentrantLock();
	private static final Point POS = new Point();
	private static final float[] COEF;
	private static final Point[] OFFS;
	
	private static IslandLayer largeIslands;
	private static IslandLayer mediumIslands;
	private static IslandLayer smallIslands;
	private static OpenSimplexNoise noise1;
	private static OpenSimplexNoise noise2;
	private static BiomeSource biomeSource;
	private static Sampler sampler;
	protected static long seed;
	
	public static void initNoise(long seed, Sampler sampler, BiomeSource biomeSource) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), GeneratorOptions.bigOptions);
		mediumIslands = new IslandLayer(random.nextInt(), GeneratorOptions.mediumOptions);
		smallIslands = new IslandLayer(random.nextInt(), GeneratorOptions.smallOptions);
		noise1 = new OpenSimplexNoise(random.nextInt());
		noise2 = new OpenSimplexNoise(random.nextInt());
		TerrainGenerator.biomeSource = biomeSource;
		TerrainGenerator.sampler = sampler;
		TerrainGenerator.seed = seed;
		TERRAIN_BOOL_CACHE_MAP.clear();
	}
	
	public static void fillTerrainDensity(double[] buffer, int posX, int posZ, double scaleXZ, double scaleY) {
		LOCKER.lock();
		
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		int x = Mth.floor(posX / scaleXZ);
		int z = Mth.floor(posZ / scaleXZ);
		double distortion1 = noise1.eval(x * 0.1, z * 0.1) * 20 + noise2.eval(x * 0.2, z * 0.2) * 10 + noise1.eval(x * 0.4, z * 0.4) * 5;
		double distortion2 = noise2.eval(x * 0.1, z * 0.1) * 20 + noise1.eval(x * 0.2, z * 0.2) * 10 + noise2.eval(x * 0.4, z * 0.4) * 5;
		double px = x * scaleXZ + distortion1;
		double pz = z * scaleXZ + distortion2;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		float height = getAverageDepth(x << 1, z << 1) * 0.5F;
		
		for (int y = 0; y < buffer.length; y++) {
			double py = y * scaleY;
			float dist = largeIslands.getDensity(px, py, pz, height);
			dist = dist > 1 ? dist : MHelper.max(dist, mediumIslands.getDensity(px, py, pz, height));
			dist = dist > 1 ? dist : MHelper.max(dist, smallIslands.getDensity(px, py, pz, height));
			if (dist > -0.5F) {
				dist += noise1.eval(px * 0.01, py * 0.01, pz * 0.01) * 0.02 + 0.02;
				dist += noise2.eval(px * 0.05, py * 0.05, pz * 0.05) * 0.01 + 0.01;
				dist += noise1.eval(px * 0.1, py * 0.1, pz * 0.1) * 0.005 + 0.005;
			}
			buffer[y] = dist;
		}
		
		LOCKER.unlock();
	}
	
	private static float getAverageDepth(int x, int z) {
		if (getBiome(x, z).getTerrainHeight() < 0.1F) {
			return 0F;
		}
		float depth = 0F;
		for (int i = 0; i < OFFS.length; i++) {
			int px = x + OFFS[i].x;
			int pz = z + OFFS[i].y;
			depth += getBiome(px, pz).getTerrainHeight() * COEF[i];
		}
		return depth;
	}
	
	private static BCLBiome getBiome(int x, int z) {
		if (biomeSource instanceof EdenBiomeSource) {
			return EdenBiomeSource.class.cast(biomeSource).getLandBiome(x, z);
		}
		return BiomeAPI.getBiome(biomeSource.getNoiseBiome(x, 0, z, sampler));
	}
	
	static {
		float sum = 0;
		List<Float> coef = Lists.newArrayList();
		List<Point> pos = Lists.newArrayList();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				float dist = MHelper.length(x, z) / 3F;
				if (dist <= 1) {
					sum += dist;
					coef.add(dist);
					pos.add(new Point(x, z));
				}
			}
		}
		OFFS = pos.toArray(new Point[] {});
		COEF = new float[coef.size()];
		for (int i = 0; i < COEF.length; i++) {
			COEF[i] = coef.get(i) / sum;
		}
	}
	
	public static Sampler getSampler() {
		return sampler;
	}
}
