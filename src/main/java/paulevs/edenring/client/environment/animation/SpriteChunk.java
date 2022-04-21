package paulevs.edenring.client.environment.animation;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpriteChunk {
	protected final List<SpriteAnimation> data = new ArrayList<>();
	protected final int chunkX;
	protected final int chunkZ;
	
	public SpriteChunk(int chunkX, int chunkZ, Random random, int count, SpriteInitializer animationInit) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		int blockX = chunkX << 4;
		int blockZ = chunkZ << 4;
		for (int i = 0; i < count; i++) {
			BlockPos p = new BlockPos(blockX | random.nextInt(16), 64 + random.nextInt(128), blockZ | random.nextInt(16));
			data.add(animationInit.init(p, random));
		}
	}
	
	public List<SpriteAnimation> getPoints() {
		return data;
	}
	
	public boolean isCorrectPos(int x, int z) {
		return chunkX == x && chunkZ == z;
	}
}
