package paulevs.edenring.world.features.terrain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.betterx.bclib.api.v2.levelgen.features.features.DefaultFeature;
import org.betterx.bclib.noise.OpenSimplexNoise;

public class StoneLayer extends DefaultFeature {
	private static final OpenSimplexNoise OFFSET_NOISE = new OpenSimplexNoise("stone".hashCode());
	private OpenSimplexNoise noise;
	private Block block;
	
	public StoneLayer(Block block) {
		noise = new OpenSimplexNoise(BuiltInRegistries.BLOCK.getKey(block).hashCode());
		this.block = block;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos center = featurePlaceContext.origin();
		
		int posX = center.getX() & 0xFFFFFFF0;
		int posZ = center.getZ() & 0xFFFFFFF0;
		ChunkAccess chunk = level.getChunk(center.getX() >> 4, center.getZ() >> 4);
		
		MutableBlockPos pos = new MutableBlockPos();
		
		for (int x = 0; x < 16; x++) {
			int wx = x | posX;
			pos.setX(x);
			for (int z = 0; z < 16; z++) {
				int wz = z | posZ;
				pos.setZ(z);
				float offset = (float) OFFSET_NOISE.eval(wx * 0.03, wz * 0.03);
				offset += (float) OFFSET_NOISE.eval(wx * 0.07, wz * 0.07) * 0.5F;
				int maxY = chunk.getHeight(Types.WORLD_SURFACE_WG, x, z);
				for (int y = 0; y < maxY; y++) {
					pos.setY(y);
					if (noise.eval(wx * 0.01, y * 0.1 + offset, wz * 0.01) > 0.45F && chunk.getBlockState(pos).is(Blocks.STONE)) {
						chunk.setBlockState(pos, block.defaultBlockState(), false);
					}
				}
			}
		}
		
		return true;
	}
}
