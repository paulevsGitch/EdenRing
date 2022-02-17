package paulevs.edenring.noise;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import paulevs.edenring.world.generator.TerrainGenerator;

import java.util.function.Function;

public class InterpolationCell {
	private final float[][][] data;
	private final BlockPos origin;
	private final int dxz;
	private final int dy;
	
	public InterpolationCell(Function<BlockPos, Float> generator, int width, int height, int dxz, int dy, BlockPos origin) {
		this.data = new float[width][width][height];
		this.origin = origin;
		this.dxz = dxz;
		this.dy = dy;
		MutableBlockPos pos = new MutableBlockPos();
		for (int x = 0; x < width; x++) {
			pos.setX(origin.getX() + x * dxz);
			for (int z = 0; z < width; z++) {
				pos.setZ(origin.getZ() + z * dxz);
				for (int y = 0; y < height; y++) {
					pos.setY(origin.getY() + y * dy);
					data[x][z][y] = generator.apply(pos);
				}
			}
		}
	}
	
	public InterpolationCell(TerrainGenerator generator, int width, int height, int dxz, int dy, BlockPos origin) {
		this.data = new float[width][width][height];
		this.origin = origin;
		this.dxz = dxz;
		this.dy = dy;
		for (int x = 0; x < width; x++) {
			int px = origin.getX() + x * dxz;
			for (int z = 0; z < width; z++) {
				int pz = origin.getZ() + z * dxz;
				generator.fillTerrainDensity(this.data[x][z], px, pz, dxz, dy);
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
		
		float a = Mth.lerp(dx, data[x1][z1][y1], data[x2][z1][y1]);
		float b = Mth.lerp(dx, data[x1][z1][y2], data[x2][z1][y2]);
		float c = Mth.lerp(dx, data[x1][z2][y1], data[x2][z2][y1]);
		float d = Mth.lerp(dx, data[x1][z2][y2], data[x2][z2][y2]);
		
		a = Mth.lerp(dy, a, b);
		b = Mth.lerp(dy, c, d);
		
		return Mth.lerp(dz, a, b);
	}
}
