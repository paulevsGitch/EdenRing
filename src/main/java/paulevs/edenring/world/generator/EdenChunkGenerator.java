package paulevs.edenring.world.generator;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.synth.SurfaceNoise;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

public class EdenChunkGenerator extends ChunkGenerator {
	public static final Codec<EdenChunkGenerator> CODEC = RecordCodecBuilder.create(
		(instance) -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter((surfaceChunkGenerator) -> surfaceChunkGenerator.runtimeBiomeSource)
		).apply(instance, instance.stable(EdenChunkGenerator::new))
	);
	
	private static final BlockState STONE = Blocks.STONE.defaultBlockState();
	private static final BlockState WATER = Blocks.WATER.defaultBlockState();
	
	private static final NoiseColumn AIR_COLUMN;
	private static SurfaceNoise surfaceNoise;
	
	public EdenChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource, biomeSource, makeSettings(), 0);
		this.surfaceNoise = new PerlinSimplexNoise(new WorldgenRandom(0), IntStream.rangeClosed(-3, 0));
		TerrainGenerator.initNoise(0);
	}
	
	private static StructureSettings makeSettings() {
		Map<StructureFeature<?>, StructureFeatureConfiguration> features = Maps.newHashMap();
		features.put(StructureFeature.VILLAGE, StructureSettings.DEFAULTS.get(StructureFeature.VILLAGE));
		return new StructureSettings(Optional.of(StructureSettings.DEFAULT_STRONGHOLD), features);
	}
	
	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}
	
	@Override
	public ChunkGenerator withSeed(long seed) {
		EdenChunkGenerator generator = new EdenChunkGenerator(this.runtimeBiomeSource.withSeed(seed));
		generator.surfaceNoise = new PerlinSimplexNoise(new WorldgenRandom(seed), IntStream.rangeClosed(-3, 0));
		TerrainGenerator.initNoise(seed);
		return generator;
	}
	
	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion worldGenRegion, ChunkAccess chunkAccess) {
		ChunkPos chunkPos = chunkAccess.getPos();
		int posX = chunkPos.x << 4;
		int posZ = chunkPos.z << 4;
		
		WorldgenRandom worldgenRandom = new WorldgenRandom();
		worldgenRandom.setBaseChunkSeed(chunkPos.x, chunkPos.z);
		
		MutableBlockPos pos = new MutableBlockPos();
		
		float[][][] buffer = new float[3][3][32];
		for (int i = 0; i < 9; i++) {
			int x = i % 3;
			int z = i / 3;
			int px = ((x << 3) + posX) >> 3;
			int pz = ((z << 3) + posZ) >> 3;
			TerrainGenerator.fillTerrainDensity(buffer[x][z], px, pz, runtimeBiomeSource);
		}
		
		for (int i = 0; i < 4; i++) {
			int x = i & 1;
			int z = i >> 1;
			
			for (int j = 0; j < 31; j++) {
				float a = buffer[x][z][j];
				float b = buffer[x + 1][z][j];
				float c = buffer[x][z + 1][j];
				float d = buffer[x + 1][z + 1][j];
				
				float e = buffer[x][z][j + 1];
				float f = buffer[x + 1][z][j + 1];
				float g = buffer[x][z + 1][j + 1];
				float h = buffer[x + 1][z + 1][j + 1];
				
				if (a < 0 && b < 0 && c < 0 && d < 0 && e < 0 && f < 0 && g < 0 && h < 0) {
					continue;
				}
				
				for (int l = 0; l < 8; l++) {
					float dx = l / 8.0F;
					pos.setX(posX + (x << 3) + l);
					
					float a1 = Mth.lerp(dx, a, b);
					float b1 = Mth.lerp(dx, c, d);
					float c1 = Mth.lerp(dx, e, f);
					float d1 = Mth.lerp(dx, g, h);
					
					for (int m = 0; m < 8; m++) {
						float dz = m / 8.0F;
						pos.setZ(posZ + (z << 3) + m);
						
						float a2 = Mth.lerp(dz, a1, b1);
						float b2 = Mth.lerp(dz, c1, d1);
						
						for (int n = 0; n < 8; n++) {
							float dy = n / 8.0F;
							pos.setY((j << 3) | n);
							
							float dens = Mth.lerp(dy, a2, b2);
							if (dens > 0) {
								chunkAccess.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
							}
						}
					}
				}
			}
		}
		
		for (int x = 0; x < 16; x++) {
			int px = posX | x;
			pos.setX(px);
			for (int z = 0; z < 16; z++) {
				int pz = posZ | z;
				pos.setZ(pz);
				int q = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
				double e = this.surfaceNoise.getSurfaceNoiseValue(px * 0.0625, pz * 0.0625, 0.0625, x * 0.0625D) * 15.0D;
				worldGenRegion.getBiome(pos).buildSurfaceAt(worldgenRandom, chunkAccess, px, pz, q, e, STONE, WATER, this.getSeaLevel(), 0, worldGenRegion.getSeed());
			}
		}
	}
	
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess) {
		return CompletableFuture.completedFuture(chunkAccess);
	}
	
	@Override
	public int getBaseHeight(int x, int z, Types types, LevelHeightAccessor accessor) {
		int posX = (x >> 3) << 3;
		int posZ = (z >> 3) << 3;
		float dx = (x - posX) / 8.0F;
		float dz = (z - posZ) / 8.0F;
		float[][][] buffer = new float[2][2][32];
		for (int i = 0; i < 4; i++) {
			int ix = i & 1;
			int iz = i >> 1;
			int px = ((ix << 3) + posX) >> 3;
			int pz = ((iz << 3) + posZ) >> 3;
			TerrainGenerator.fillTerrainDensity(buffer[ix][iz], px, pz, runtimeBiomeSource);
		}
		
		for (int j = 30; j >= 0; j--) {
			float a = buffer[0][0][j];
			float b = buffer[1][0][j];
			float c = buffer[0][1][j];
			float d = buffer[1][1][j];
			
			float e = buffer[0][0][j + 1];
			float f = buffer[1][0][j + 1];
			float g = buffer[0][1][j + 1];
			float h = buffer[1][1][j + 1];
			
			if (a < 0 && b < 0 && c < 0 && d < 0 && e < 0 && f < 0 && g < 0 && h < 0) {
				continue;
			}
			
			a = Mth.lerp(dx, a, b);
			b = Mth.lerp(dx, c, d);
			c = Mth.lerp(dx, e, f);
			d = Mth.lerp(dx, g, h);
			
			a = Mth.lerp(dz, a, b);
			b = Mth.lerp(dz, c, d);
			
			for (int n = 7; n >= 0; n--) {
				float dy = n / 8.0F;
				float dens = Mth.lerp(dy, a, b);
				if (dens > 0) {
					return (j << 3 | n) + 1;
				}
			}
		}
		
		return -256;
	}
	
	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor accessor) {
		return AIR_COLUMN;
	}
	
	@Override
	public int getSeaLevel() {
		return 0;
	}
	
	static {
		BlockState[] states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.defaultBlockState());
		AIR_COLUMN = new NoiseColumn(0, states);
	}
}
