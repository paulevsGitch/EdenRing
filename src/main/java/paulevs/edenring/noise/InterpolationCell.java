package paulevs.edenring.noise;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;

import java.util.function.Function;

public class InterpolationCell {
	private final float[][][] data;
	private final BlockPos origin;
	private final int dxz;
	private final int dy;
	
	public InterpolationCell(Function<BlockPos, Float> generator, int width, int height, int dxz, int dy, BlockPos origin) {
		this.data = new float[width][height][width];
		this.origin = origin;
		this.dxz = dxz;
		this.dy = dxz;
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = 0; x < width; x++) {
			pos.setX(origin.getX() + x * dxz);
			for (int z = 0; z < width; z++) {
				pos.setZ(origin.getZ() + z * dxz);
				for (int y = 0; y < height; y++) {
					pos.setY(origin.getY() + y * dy);
					data[x][y][z] = generator.apply(pos);
				}
			}
		}
	}
	
	public float get(BlockPos pos, boolean relative) {
		if (relative) {
			return get(pos.getX(), pos.getY(), pos.getZ());
		}
		return get(pos.getX() - origin.getX(), pos.getY() - origin.getY(), pos.getZ() - origin.getZ());
	}
	
	private float get(int x, int y, int z) {
		int x1 = x / dxz;
		int y1 = y / dy;
		int z1 = z / dxz;
		int x2 = x1 + 1;
		int y2 = y1 + 1;
		int z2 = z1 + 1;
		float dx = (float) x / dxz - x1;
		float dy = (float) y / this.dy - y1;
		float dz = (float) z / dxz - z1;
		
		float a = Mth.lerp(dx, data[x1][y1][z1], data[x2][y1][z1]);
		float b = Mth.lerp(dx, data[x1][y2][z1], data[x2][y2][z1]);
		float c = Mth.lerp(dx, data[x1][y1][z2], data[x2][y1][z2]);
		float d = Mth.lerp(dx, data[x1][y2][z2], data[x2][y2][z2]);
		
		a = Mth.lerp(dy, a, b);
		b = Mth.lerp(dy, c, d);
		
		return Mth.lerp(dz, a, b);
	}
}
