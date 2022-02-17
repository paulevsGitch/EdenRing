package paulevs.edenring.world.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import paulevs.edenring.noise.InterpolationCell;
import paulevs.edenring.noise.VoronoiNoise;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

import java.util.Random;

public class CaveGenerator {
	private static final BlockState CAVE_AIR = /*Blocks.GLASS.defaultBlockState();*/Blocks.CAVE_AIR.defaultBlockState();
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
	private static int seed;
	
	public static void init(long seed) {
		Random random = new Random(seed);
		simplexNoise = new OpenSimplexNoise(random.nextInt());
		CaveGenerator.seed = random.nextInt();
	}
	
	public static void carve(ChunkAccess chunkAccess) {
		//if (true) return;
		
		long time = System.currentTimeMillis();
		
		int minX = chunkAccess.getPos().getMinBlockX();
		int minZ = chunkAccess.getPos().getMinBlockZ();
		int minY = chunkAccess.getMinBuildHeight();
		int maxY = chunkAccess.getMaxBuildHeight();
		
		final float[] buffer = new float[27];
		
		Random rand = new Random();
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		InterpolationCell cellSparce = new InterpolationCell(generator, 4, (maxY - minY) / 16 + 1, 16, 16, new BlockPos(minX - 16, minY, minZ - 16));
		InterpolationCell cellTerrain = new InterpolationCell(generator, 3, (maxY - minY) / 8 + 1, 8, 8, new BlockPos(minX, minY, minZ));
		InterpolationCell cellVoronoi = new InterpolationCell(p -> getTunelNoise(p, buffer, rand), 5, (maxY - minY) / 4 + 1, 4, 4, new BlockPos(minX, minY, minZ));
		InterpolationCell cellBigCave = new InterpolationCell(p -> getBigCaveNoise(cellSparce, p, 0), 5, (maxY - minY) / 4 + 1, 4, 4, new BlockPos(minX, minY, minZ));
		
		MutableBlockPos pos = new MutableBlockPos();
		//MutableBlockPos posWorld = new MutableBlockPos();
		
		for (byte x = 0; x < 16; x++) {
			//posWorld.setX(minX | x);
			pos.setX(x);
			for (byte z = 0; z < 16; z++) {
				//posWorld.setZ(minZ | z);
				pos.setZ(z);
				
				byte index = 0;
				float[] accumulation = new float[8];
				
				int max = chunkAccess.getHeight(Types.WORLD_SURFACE_WG, x, z) - 10;
				
				for (int y = minY; y < max; y++) {
					pos.setY(y);
					
					if (chunkAccess.getBlockState(pos).getBlock() == Blocks.STONE) {
						float noise = cellVoronoi.get(pos, true);
						accumulation[index++] = noise;
						if (index >= accumulation.length) {
							index = 0;
						}
						
						float average = 0;
						for (byte i = 0; i < accumulation.length; i++) {
							noise = MHelper.max(noise, accumulation[i]);
							average += accumulation[i];
						}
						noise = (noise + (average / accumulation.length)) * 0.5F - 0.9F;
						
						float cellValue = cellTerrain.get(pos, true);
						noise = -smoothUnion(-noise, cellValue + 0.5F, 1.1F);
						
						if (noise < 0.1F) {
							noise = MHelper.max(noise, cellBigCave.get(pos, true));
						}
						
						if (noise > 0) {
							chunkAccess.setBlockState(pos, CAVE_AIR, false);
							int py = pos.getY();
							pos.setY(py + 1);
							if (!chunkAccess.getBlockState(pos).isAir()) {
								pos.setY(py + 3);
								if (chunkAccess.getBlockState(pos).isAir()) {
									for (byte i = 1; i < 3; i++) {
										pos.setY(py + i);
										chunkAccess.setBlockState(pos, CAVE_AIR, false);
									}
								}
								else {
									pos.setY(py + 4);
									if (chunkAccess.getBlockState(pos).isAir()) {
										pos.setY(py + 3);
										chunkAccess.setBlockState(pos, CAVE_AIR, false);
									}
								}
							}
						}
					}
				}
			}
		}
		
		/*for (int x = 0; x < 16; x++) {
			posWorld.setX(minX | x);
			pos.setX(x);
			for (int z = 0; z < 16; z++) {
				posWorld.setZ(minZ | z);
				pos.setZ(z);
				
				byte index = 0;
				float[] accumulation = new float[8];
				
				for (int y = minY; y < maxY; y++) {
					posWorld.setY(y);
					pos.setY(y - minY);
					float cellValue = cell.get(posWorld, false);
					//float cellValue = cell.get(pos, true);
					if (cellValue > -0.6F) {
						//if (isTerrain(chunkAccess.getBlockState(pos))) {
						float noise = cellVoronoi.get(pos, true);//0;//getTunelNoise(posWorld, buffer, rand);
						accumulation[index++] = noise;
						if (index >= accumulation.length) {
							index = 0;
						}
						
						float average = 0;
						for (byte i = 0; i < accumulation.length; i++) {
							noise = MHelper.max(noise, accumulation[i]);
							average += accumulation[i];
						}
						noise = (noise + (average / accumulation.length)) * 0.5F - 0.9F;
						noise = -smoothUnion(-noise, cellValue + 0.5F, 1.1F);
						
						if (noise < 0.1F) {
							float bigCave = getBigCaveNoise(cell, posWorld, cellValue);
							noise = MHelper.max(noise, bigCave);
						}
						
						//noise = bigCave;
						//noise = -smoothUnion(-noise, -bigCave, 1.2F);
						
						if (noise > 0) {
							chunkAccess.setBlockState(pos, CAVE_AIR, false);
							int py = pos.getY();
							pos.setY(py + 1);
							if (!chunkAccess.getBlockState(pos).isAir()) {
								pos.setY(py + 3);
								if (chunkAccess.getBlockState(pos).isAir()) {
									for (byte i = 1; i < 3; i++) {
										pos.setY(py + i);
										chunkAccess.setBlockState(pos, CAVE_AIR, false);
									}
								}
								else {
									pos.setY(py + 4);
									if (chunkAccess.getBlockState(pos).isAir()) {
										pos.setY(py + 3);
										chunkAccess.setBlockState(pos, CAVE_AIR, false);
									}
								}
							}
						}
					}
				}
			}
		}*/
		
		time = System.currentTimeMillis() - time;
		System.out.println(time);
	}
	
	private static float getTunelNoise(BlockPos pos, float[] buffer, Random random) {
		VORONOI_NOISE.getDistances(seed, pos.getX() * 0.01, pos.getY() * 0.03, pos.getZ() * 0.01, buffer, random);
		return buffer[0] / buffer[2];
		//return 0;
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
	
	private static float getBigCaveNoise(InterpolationCell cell, BlockPos pos, float cellValue) {
		//return cellValue - 0.03F;// + 0.1F;
		/*if (cellValue < 0.1F) {
			return 0;
		}
		return 1.0F;*/
		//float value = (float) simplexNoise.eval(pos.getX() * 0.01, pos.getY() * 0.01, pos.getZ() * 0.01);
		//value += (float) simplexNoise.eval(pos.getX() * 0.07, pos.getY() * 0.07, pos.getZ() * 0.07) * 0.3F;
		//return Mth.clamp(value, 0, 1) * (cellValue - 0.3F) / 0.7F;
		
		if (pos.getY() < 32 || pos.getY() > 224) {
			return 0;
		}
		
		/*float noiseX = (float) simplexNoise.eval(pos.getY() * 0.1, pos.getZ() * 0.1) * 3F + 0.5F;
		float noiseY = (float) simplexNoise.eval(pos.getX() * 0.1, pos.getZ() * 0.1) * 3F + 0.5F;
		float noiseZ = (float) simplexNoise.eval(pos.getX() * 0.1, pos.getY() * 0.1) * 3F + 0.5F;
		pos = pos.offset(noiseX, noiseY, noiseZ);*/
		
		float value = cell.get(pos, false) - 0.03F;
		/*if (value < 0.02F) {
			return 0;
		}*/
		
		/*for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (cell.get(pos.relative(dir, 15), false) < 0) {
				value = 0;
				break;
			}
		}*/
		
		/*for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (cell.get(pos.relative(dir, 10), false) < 0) {
				value = 0;
				break;
			}
		}*/
		
		float noise = (float) simplexNoise.eval(pos.getX() * 0.01, pos.getY() * 0.01, pos.getZ() * 0.01);
		//noise = Mth.clamp(noise * 5, 0, 1);
		//value = -smoothUnion(-value, -noise, 1);
		//value -= Mth.clamp(noise * 2, 0, 1);
		value -= Mth.clamp(noise * 0.5F, 0, 1);
		//value *= noise;
		
		noise = (float) simplexNoise.eval(pos.getX() * 0.1, pos.getY() * 0.1, pos.getZ() * 0.1);
		value += noise * 0.01F;
		
		if (value > 0) {
			for (Direction dir : BlocksHelper.DIRECTIONS) {
				if (cell.get(pos.relative(dir, 7), false) < 0) {
					value -= 1;
					break;
				}
			}
		}
		
		//value = Mth.clamp(value, 0, 1);
		return value;
	}
}
