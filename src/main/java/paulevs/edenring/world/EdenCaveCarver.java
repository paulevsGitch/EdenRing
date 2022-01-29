package paulevs.edenring.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import paulevs.edenring.noise.InterpolationCell;
import paulevs.edenring.noise.VoronoiNoise;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.generator.MultiThreadGenerator;
import paulevs.edenring.world.generator.TerrainGenerator;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.util.MHelper;

import java.util.Random;
import java.util.function.Function;

public class EdenCaveCarver extends WorldCarver<CaveCarverConfiguration> {
	private static final VoronoiNoise NOISE = new VoronoiNoise();
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
	
	public EdenCaveCarver(Codec<CaveCarverConfiguration> codec) {
		super(codec);
	}
	
	@Override
	public boolean carve(CarvingContext carvingContext, CaveCarverConfiguration carverConfiguration, ChunkAccess chunkAccess, Function<BlockPos, Biome> function, Random random, Aquifer aquifer, ChunkPos chunkPos, CarvingMask carvingMask) {
		if (!chunkAccess.getPos().equals(chunkPos)) {
			return false;
		}
		
		int minX = chunkPos.getMinBlockX();
		int minZ = chunkPos.getMinBlockZ();
		int minY = chunkAccess.getMinBuildHeight();
		int maxY = chunkAccess.getMaxBuildHeight();
		
		final float[] buffer = new float[27];
		
		Random rand = new Random();
		MutableBlockPos pos = new MutableBlockPos();
		MutableBlockPos posWorld = new MutableBlockPos();
		
		TerrainGenerator generator = MultiThreadGenerator.getTerrainGenerator();
		InterpolationCell cell = new InterpolationCell(generator, 3, (maxY - minY) / 8, 8, 8, new BlockPos(minX, minY, minZ));
		
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
					pos.setY(y);
					if (isTerrain(chunkAccess.getBlockState(pos))) {
						float noise = getNoise(posWorld, buffer, rand);
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
						
						noise = -smoothUnion(-noise, cellValue, 1.1F);
						
						if (noise > 0.9F) {
							chunkAccess.setBlockState(pos, CAVE_AIR, false);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isStartChunk(CaveCarverConfiguration carverConfiguration, Random random) {
		return true;
	}
	
	private float getNoise(BlockPos pos, float[] buffer, Random random) {
		NOISE.getDistances(0, pos.getX() * 0.01, pos.getY() * 0.03, pos.getZ() * 0.01, buffer, random);
		return buffer[0] / buffer[2];
	}
	
	private boolean isTerrain(BlockState state) {
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
	
	private float smoothUnion(float a, float b, float radius) {
		float h = Mth.clamp(0.5F + 0.5F * (b - a) / radius, 0F, 1F);
		return Mth.lerp(h, b, a) - radius * h * (1F - h);
	}
}
