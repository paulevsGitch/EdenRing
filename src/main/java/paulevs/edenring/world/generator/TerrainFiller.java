package paulevs.edenring.world.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import paulevs.edenring.noise.InterpolationCell;

public class TerrainFiller {
	private static final BlockState STONE = Blocks.STONE.defaultBlockState();
	
	public static InterpolationCell fill(ChunkAccess chunkAccess) {
		final short minY = (short) chunkAccess.getMinBuildHeight();
		final short maxY = (short) chunkAccess.getMaxBuildHeight();
		ChunkPos chunkPos = chunkAccess.getPos();
		final BlockPos origin = new BlockPos(chunkPos.getMinBlockX(), chunkAccess.getMinBuildHeight(), chunkPos.getMinBlockZ());
		final short maxCell = (short) ((maxY - minY) / 8 + 1);
		
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		InterpolationCell cellTerrain = new InterpolationCell(generator, 3, maxCell, 8, 8, origin);
		
		MutableBlockPos pos = new MutableBlockPos();
		for (byte x = 0; x < 16; x++) {
			pos.setX(x);
			for (byte z = 0; z < 16; z++) {
				pos.setZ(z);
				for (short y = minY; y < maxY; y++) {
					pos.setY(y);
					if (cellTerrain.get(pos, true) > 0) {
						chunkAccess.setBlockState(pos, STONE, false);
					}
				}
			}
		}
		
		return cellTerrain;
	}
}
