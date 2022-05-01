package paulevs.edenring.noise;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import paulevs.edenring.world.generator.TerrainGenerator;

import java.util.function.Function;

public class InterpolationCell {
	private final float[][][] data;
	private final BlockPos origin;
	private final short dxz;
	private final short dy;
	
	public InterpolationCell(Function<BlockPos, Float> generator, int width, int height, int dxz, int dy, BlockPos origin) {
		this.data = new float[width][width][height];
		this.origin = origin;
		this.dxz = (short) dxz;
		this.dy = (short) dy;
		
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
		this.dxz = (short) dxz;
		this.dy = (short) dy;
		
		MutableBlockPos pos = origin.mutable();
		for (int x = 0; x < width; x++) {
			pos.setX(origin.getX() + x * dxz);
			for (int z = 0; z < width; z++) {
				pos.setZ(origin.getZ() + z * dxz);
				generator.fillTerrainDensity(this.data[x][z], pos, dxz, dy);
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
	
	public short getMinY() {
		int max = data[0][0].length;
		for (short y = 0; y < max; y++) {
			if (checkSlice(y)) return getY(y - 1);
		}
		return getY(max - 1);
	}
	
	public short getMaxY() {
		for (short y = (short) (data[0][0].length - 1); y >= 0; y--) {
			if (checkSlice(y)) return getY(y + 1);
		}
		return (short) origin.getY();
	}
	
	private short getY(int y) {
		return (short) (y * dy + origin.getY());
	}
	
	private boolean checkSlice(short y) {
		for (int x = 0; x < data.length; x++) {
			for (int z = 0; z < data[0].length; z++) {
				if (data[x][z][y] > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
