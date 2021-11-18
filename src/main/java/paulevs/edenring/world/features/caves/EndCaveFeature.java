package paulevs.edenring.world.features.caves;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.generator.EdenBiomeSource;
import ru.bclib.interfaces.BiomeSetter;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public abstract class EndCaveFeature extends DefaultFeature {
	public static EdenBiomeSource BIOME_SOURCE;
	
	protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
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
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final WorldGenLevel level = featureConfig.level();
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		
		if (BIOME_SOURCE == null) {
			return false;
		}
		
		BlockPos center = findPos(level, pos, random);
		
		if (center == null) {
			return false;
		}
		
		BCLBiome biome = BIOME_SOURCE.getCaveBiome(pos.getX(), pos.getZ());
		generateTerrain(level, center, biome, random);
		
		//biome.getActualBiome().get
		
		return true;
	}
	
	protected void setBiome(WorldGenLevel world, BlockPos pos, BCLBiome biome) {
		ChunkAccess chunk = world.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.EMPTY, false);
		if (chunk != null) {
			BiomeSetter array = (BiomeSetter) chunk.getBiomes();
			if (array != null) {
				array.bclib_setBiome(biome.getActualBiome(), pos);
			}
		}
	}
	
	protected boolean isTerrain(BlockState state) {
		for (Block stone: TERRAIN_BLOCKS) {
			if (state.is(stone)) {
				return true;
			}
		}
		return false;
	}
	
	protected abstract BlockPos findPos(WorldGenLevel world, BlockPos pos, Random random);
	
	protected abstract void generateTerrain(WorldGenLevel world, BlockPos center, BCLBiome biome, Random random);
}
