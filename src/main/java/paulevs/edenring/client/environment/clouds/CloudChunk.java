package paulevs.edenring.client.environment.clouds;

import net.minecraft.core.BlockPos;
import ru.bclib.util.MHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudChunk {
	private final List<CloudAnimation> data;
	private final int blockX;
	private final int blockZ;
	private final int chunkX;
	private final int chunkZ;
	
	public CloudChunk(int chunkX, int chunkZ, Random random, int count) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.blockX = chunkX << 4;
		this.blockZ = chunkZ << 4;
		data = new ArrayList<>(count);
		for (byte i = 0; i < count; i++) {
			BlockPos p = new BlockPos(blockX | random.nextInt(16), 64 + random.nextInt(128), blockZ | random.nextInt(16));
			data.add(new CloudAnimation(
				p,
				random.nextInt(4096),
				(byte) random.nextInt(4),
				MHelper.randRange(10.0F, 20.0F, random),
				MHelper.randRange(3000, 5000, random)
			));
		}
	}
	
	public List<CloudAnimation> getPoints() {
		return data;
	}
	
	public boolean isCorrectPos(int x, int z) {
		return chunkX == x && chunkZ == z;
	}
}
