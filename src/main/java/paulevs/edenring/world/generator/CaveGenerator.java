package paulevs.edenring.world.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import paulevs.edenring.noise.InterpolationCell;
import paulevs.edenring.noise.VoronoiNoise;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.MHelper;

import java.util.Random;

public class CaveGenerator {
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
	private static final VoronoiNoise VORONOI_NOISE = new VoronoiNoise();
	private static final Block[] TERRAIN_BLOCKS = new Block[] {
		Blocks.STONE,
		Blocks.GRANITE,
		Blocks.ANDESITE,
		Blocks.DIORITE,
		Blocks.CALCITE,
		Blocks.TUFF,
		Blocks.DEEPSLATE,
		Blocks.IRON_ORE,
		Blocks.GOLD_ORE,
		Blocks.COPPER_ORE,
		Blocks.RAW_IRON_BLOCK,
		Blocks.RAW_GOLD_BLOCK,
		Blocks.RAW_COPPER_BLOCK,
		Blocks.DIRT,
		EdenBlocks.EDEN_GRASS_BLOCK
	};
	private static OpenSimplexNoise simplexNoise;
	
	public static void init(long seed) {
		simplexNoise = new OpenSimplexNoise(seed);
	}
	
	public static void carve(ChunkAccess chunkAccess) {
		int minX = chunkAccess.getPos().getMinBlockX();
		int minZ = chunkAccess.getPos().getMinBlockZ();
		int minY = chunkAccess.getMinBuildHeight();
		int maxY = chunkAccess.getMaxBuildHeight();
		
		final float[] buffer = new float[27];
		
		Random rand = new Random();
		MutableBlockPos pos = new MutableBlockPos();
		MutableBlockPos posWorld = new MutableBlockPos();
		
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		InterpolationCell cell = new InterpolationCell(generator, 3, (maxY - minY) / 8 + 1, 8, 8, new BlockPos(minX, minY, minZ));
		
		for (int x = 0; x < 16; x++) {
			posWorld.setX(minX | x);
			pos.setX(x);
			for (int z = 0; z < 16; z++) {
				posWorld.setZ(minZ | z);
				pos.setZ(z);
				
				byte index = 0;
				float[] accumulation = new float[8];
				
				for (int y = minY; y < maxY; y++) {
					posWorld.setY(minY + y);
					pos.setY(y - minY);
					if (isTerrain(chunkAccess.getBlockState(pos))) {
						float noise = getTunelNoise(posWorld, buffer, rand);
						accumulation[index++] = noise;
						if (index >= accumulation.length) {
							index = 0;
						}
						
						float average = 0;
						for (byte i = 0; i < accumulation.length; i++) {
							noise = MHelper.max(noise, accumulation[i]);
							average += accumulation[i];
						}
						noise = (noise + (average / accumulation.length)) * 0.5F;
						float cellValue = cell.get(pos, true);
						float bigCave = getBigCaveNoise(posWorld, cellValue);
						noise = -smoothUnion(-noise, -bigCave, 1.2F);
						noise = -smoothUnion(-noise, cellValue, 1.1F);
						
						if (noise > 0.9F) {
							chunkAccess.setBlockState(pos, CAVE_AIR, false);
						}
					}
				}
			}
		}
	}
	
	private static float getTunelNoise(BlockPos pos, float[] buffer, Random random) {
		VORONOI_NOISE.getDistances(0, pos.getX() * 0.01, pos.getY() * 0.03, pos.getZ() * 0.01, buffer, random);
		return buffer[0] / buffer[2];
	}
	
	private static boolean isTerrain(BlockState state) {
		if (state.is(BlockTags.STONE_ORE_REPLACEABLES) || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)) {
			return true;
		}
		for (Block terrain: TERRAIN_BLOCKS) {
			if (state.is(terrain)) {
				return true;
			}
		}
		return false;
	}
	
	private static float smoothUnion(float a, float b, float radius) {
		float h = Mth.clamp(0.5F + 0.5F * (b - a) / radius, 0F, 1F);
		return Mth.lerp(h, b, a) - radius * h * (1F - h);
	}
	
	private static float getBigCaveNoise(BlockPos pos, float cellValue) {
		return 0F;
		/*if (cellValue < 0.1F) {
			return 0;
		}
		return 1.0F;*/
		//float value = (float) simplexNoise.eval(pos.getX() * 0.01, pos.getY() * 0.01, pos.getZ() * 0.01);
		//value += (float) simplexNoise.eval(pos.getX() * 0.07, pos.getY() * 0.07, pos.getZ() * 0.07) * 0.3F;
		//return Mth.clamp(value, 0, 1) * (cellValue - 0.3F) / 0.7F;
	}
}
