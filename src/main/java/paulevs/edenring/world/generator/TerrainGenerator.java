package paulevs.edenring.world.generator;

import com.google.common.collect.Lists;
import net.minecraft.util.Mth;
import ru.bclib.util.MHelper;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class TerrainGenerator {
	private static final float[] COEF;
	private static final Point[] OFFS;
	
	private final IslandLayer largeIslands;
	private final IslandLayer mediumIslands;
	private final IslandLayer smallIslands;
	
	public TerrainGenerator(long seed) {
		Random random = new Random(seed);
		largeIslands = new IslandLayer(random.nextInt(), GeneratorOptions.bigOptions);
		mediumIslands = new IslandLayer(random.nextInt(), GeneratorOptions.mediumOptions);
		smallIslands = new IslandLayer(random.nextInt(), GeneratorOptions.smallOptions);
	}
	
	public void fillTerrainDensity(double[] buffer, int posX, int posZ, double scaleXZ, double scaleY, float[] floatBuffer) {
		fillTerrainDensity(floatBuffer, posX, posZ, scaleXZ, scaleY);
		for (short i = 0; i < buffer.length; i++) {
			buffer[i] = floatBuffer[i];
		}
	}
	
	public float sample(int posX, int posY, int posZ) {
		largeIslands.updatePositions(posX, posZ);
		mediumIslands.updatePositions(posX, posZ);
		smallIslands.updatePositions(posX, posZ);
		
		float dist = largeIslands.getDensity(posX, posY, posZ);
		if (dist < 0.3F) {
			dist = MHelper.max(dist, mediumIslands.getDensity(posX, posY, posZ));
		}
		if (dist < 0.3F) {
			dist = MHelper.max(dist, smallIslands.getDensity(posX, posY, posZ));
		}
		return dist;
	}
	
	public void fillTerrainDensity(float[] buffer, int posX, int posZ, double scaleXZ, double scaleY) {
		largeIslands.clearCache();
		mediumIslands.clearCache();
		smallIslands.clearCache();
		
		int x = Mth.floor(posX / scaleXZ);
		int z = Mth.floor(posZ / scaleXZ);
		double px = x * scaleXZ;
		double pz = z * scaleXZ;
		
		largeIslands.updatePositions(px, pz);
		mediumIslands.updatePositions(px, pz);
		smallIslands.updatePositions(px, pz);
		
		for (int y = 0; y < buffer.length; y++) {
			double py = y * scaleY;
			float dist = largeIslands.getDensity(px, py, pz);
			if (dist < 0.3F) {
				dist = MHelper.max(dist, mediumIslands.getDensity(px, py, pz));
			}
			if (dist < 0.3F) {
				dist = MHelper.max(dist, smallIslands.getDensity(px, py, pz));
			}
			buffer[y] = dist;
		}
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
}
