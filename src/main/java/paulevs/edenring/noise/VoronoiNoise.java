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
	
	private void getPoint(int seed, int x, int y, int z, Random random, float[] point) {
		random.setSeed(MHelper.getSeed(seed, x, y, z));
		point[0] = random.nextFloat();
		point[1] = random.nextFloat();
		point[2] = random.nextFloat();
	}
}
