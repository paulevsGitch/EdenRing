package paulevs.edenring.world.features.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.noise.VoronoiNoise;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class SDFCave extends DefaultFeature {
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
	private static final VoronoiNoise NOISE = new VoronoiNoise();
	//private static SDF sdf = makeSDF();
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		ChunkAccess chunk = level.getChunk(center);
		int minX = center.getX() & 0xFFFFFFF0;
		int minZ = center.getZ() & 0xFFFFFFF0;
		int minY = level.getMinBuildHeight();
		int maxY = level.getMaxBuildHeight();
		
		Random random = new Random();
		final float[] buffer = new float[27];
		/*InterpolationCell cell = new InterpolationCell(pos -> {
			NOISE.getDistances(0, pos.getX() * 0.01, pos.getY() * 0.01, pos.getZ() * 0.01, buffer, random);
			float f1f2 = buffer[0] / buffer[2];
			return f1f2 > 0.85F ? 1.0F : 0.0F;
		}, 5, 65, 4, 4, new BlockPos(minX, minY, minZ));
		
		Function<BlockPos, Float> test = pos -> {
			NOISE.getDistances(0, pos.getX() * 0.01, pos.getY() * 0.01, pos.getZ() * 0.01, buffer, random);
			float f1f2 = buffer[0] / buffer[2];
			return f1f2 > 0.85F ? 1.0F : 0.0F;
		};*/
		
		BlockState diamond = Blocks.DIAMOND_BLOCK.defaultBlockState();
		MutableBlockPos pos = new MutableBlockPos();
		MutableBlockPos posWorld = new MutableBlockPos();
		
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
					if (isTerrain(chunk.getBlockState(pos))) {
						float noise = getNoise(posWorld, buffer, random);
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
						
						if (noise > 0.9F) {
							chunk.setBlockState(pos, CAVE_AIR, false);
						}
						/*else if (noise > 0.85F) {
							chunk.setBlockState(pos, diamond, false);
						}*/
					}
				}
			}
		}
		
		return true;
	}
	
	private float getNoise(BlockPos pos, float[] buffer, Random random) {
		NOISE.getDistances(0, pos.getX() * 0.01, pos.getY() * 0.03, pos.getZ() * 0.01, buffer, random);
		return buffer[0] / buffer[2];
	}
	
	private boolean isTerrain(BlockState state) {
		return state.is(BlockTags.STONE_ORE_REPLACEABLES) ||
			state.is(Blocks.CALCITE) ||
			state.is(Blocks.DIRT) ||
			state.is(EdenBlocks.EDEN_GRASS_BLOCK) ||
			state.is(Blocks.MOSS_BLOCK) ||
			state.is(Blocks.COBBLESTONE) ||
			state.is(Blocks.MOSSY_COBBLESTONE);
	}
	
	/*private static SDF makeSDF() {
		SDF sdf = new NoiseSDF(0, new Vector3f(0.01F, 0.015F, 0.01F));
		SDFScale
		sdf = new SDFBinary()sdf, new NoiseSDF(0, new Vector3f(0.1F, 0.1F, 0.1F)))
		return sdf;
	}
	
	private static class NoiseSDF extends SDFUnary {
		private final OpenSimplexNoise noise;
		private Vector3f scale;
		
		NoiseSDF(long seed, Vector3f scale) {
			this.noise = new OpenSimplexNoise(seed);
			this.scale = scale;
		}
		
		@Override
		public float getDistance(float x, float y, float z) {
			return (float) noise.eval(x * scale.x(), y * scale.y(), z * scale.z());
		}
	}*/
}
