package paulevs.edenring.noise;

import net.minecraft.util.Mth;
import ru.bclib.util.MHelper;

import java.util.Arrays;
import java.util.Random;

public class VoronoiNoise {
	public void getDistances(int seed, double x, double y, double z, float[] buffer, Random random) {
		int x1 = Mth.floor(x);
		int y1 = Mth.floor(y);
		int z1 = Mth.floor(z);
		float sdx = (float) (x - x1);
		float sdy = (float) (y - y1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		float[] point = new float[3];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int k = -1; k < 2; k++) {
					getPoint(seed, x1 + i, y1 + j, z1 + k, random, point);
					float dx = point[0] + i - sdx;
					float dy = point[1] + j - sdy;
					float dz = point[2] + k - sdz;
					float distance = (float) Mth.length(dx, dy, dz);
					buffer[index++] = distance;
				}
			}
		}
		Arrays.sort(buffer);
	}
	
	public void getDistances(int seed, double x, double z, float[] buffer, Random random) {
		int x1 = Mth.floor(x);
		int z1 = Mth.floor(z);
		float sdx = (float) (x - x1);
		float sdz = (float) (z - z1);
		
		byte index = 0;
		float[] point = new float[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				getPoint(seed, x1 + i, z1 + j, random, point);
				float dx = point[0] + i - sdx;
				float dz = point[1] + j - sdz;
				float distance = (float) Mth.length(dx, dz);
				buffer[index++] = distance;
			}
		}
		Arrays.sort(buffer);
	}
	
	public float getValue(int seed, double x, double z, Random random) {
		int x1 = Mth.floor(x);
		int z1 = Mth.floor(z);
		float sdx = (float) (x - x1);
		float sdz = (float) (z - z1);
		
		float distance = 10;
		float[] point = new float[2];
		int[] pointResult = new int[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				getPoint(seed, x1 + i, z1 + j, random, point);
				float dx = point[0] + i - sdx;
				float dz = point[1] + j - sdz;
				float d = (float) Mth.length(dx, dz);
				if (d < distance) {
					distance = d;
					pointResult[0] = i;
					pointResult[1] = j;
				}
			}
		}
		
		random.setSeed(MHelper.getSeed(seed, x1 + pointResult[0], z1 + pointResult[1]));
		return random.nextFloat();
	}
	
	private void getPoint(int seed, int x, int y, int z, Random random, float[] point) {
		random.setSeed(MHelper.getSeed(seed, x, y, z));
		point[0] = random.nextFloat();
		point[1] = random.nextFloat();
		point[2] = random.nextFloat();
	}
	
	private void getPoint(int seed, int x, int z, Random random, float[] point) {
		random.setSeed(MHelper.getSeed(seed, x, z));
		point[0] = random.nextFloat();
		point[1] = random.nextFloat();
	}
}
