package paulevs.edenring.world.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction.SimpleFunction;

public class EdenDensityFunction implements SimpleFunction {
	public static final KeyDispatchDataCodec<EdenDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new EdenDensityFunction(0L)));
	
	public EdenDensityFunction(long seed) {}
	
	@Override
	public double compute(FunctionContext context) {
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		return generator.getTerrainDensity(context.blockX(), context.blockY(), context.blockZ());
	}
	
	/*@Override
	public void fillArray(double[] ds, ContextProvider contextProvider) {
		//contextProvider.fillAllDirectly(ds, this);
		//TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		// double[] buffer, BlockPos pos, double scaleXZ, double scaleY, float[] floatBuffer
		//generator.fillTerrainDensity(ds, contextProvider.);
		
		
		int arrayIndex = 0;
		for (int i = this.cellHeight - 1; i >= 0; --i) {
			this.inCellY = i;
			for (int j = 0; j < this.cellWidth; ++j) {
				this.inCellX = j;
				int k = 0;
				while (k < this.cellWidth) {
					this.inCellZ = k++;
					ds[this.arrayIndex++] = densityFunction.compute(this);
				}
			}
		}
	}*/
	
	/*@Override
	public DensityFunction mapAll(Visitor visitor) {
		return null;
	}*/
	
	@Override
	public double minValue() {
		return -1.0;
	}
	
	@Override
	public double maxValue() {
		return 1.0;
	}
	
	@Override
	public KeyDispatchDataCodec<? extends DensityFunction> codec() {
		return CODEC;
	}
}
