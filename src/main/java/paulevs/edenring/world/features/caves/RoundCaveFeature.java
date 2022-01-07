package paulevs.edenring.world.features.caves;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;

import java.util.Random;

public class RoundCaveFeature extends EndCaveFeature {
	@Override
	protected void generateTerrain(WorldGenLevel world, BlockPos center, BCLBiome biome, Random random) {
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		
		int radius = MHelper.randRange(5, 12, random);
		int radiusY = MHelper.floor(radius / 1.6F);
		
		MutableBlockPos mpos = new MutableBlockPos();
		for (int y = -radiusY; y <= radiusY; y++) {
			int y2 = MHelper.floor(y * y * 2.56F);
			mpos.setY(y + center.getY());
			for (int x = -radius; x <= radius; x++) {
				int x2 = x * x;
				mpos.setX(x + center.getX());
				for (int z = -radius; z <= radius; z++) {
					int z2 = z * z;
					mpos.setZ(z + center.getZ());
					float sqr = Mth.sqrt(x2 + y2 + z2);
					if (sqr < radius - noise.eval(x * 0.1, y * 0.1, z * 0.1) * 5) {
						BlocksHelper.setWithoutUpdate(world, mpos, CAVE_AIR);
						BiomeAPI.setBiome(world, mpos, biome.getActualBiome());
					}
				}
			}
		}
	}
	
	protected BlockPos findPos(WorldGenLevel world, BlockPos pos, Random random) {
		int px = (pos.getX() & 0xFFFFFFF0) | random.nextInt(16);
		int pz = (pos.getZ() & 0xFFFFFFF0) | random.nextInt(16);
		
		ChunkAccess chunk = world.getChunk(px >> 4, pz >> 4);
		int py = chunk.getHeight(Types.WORLD_SURFACE_WG, px & 15, pz & 15);
		if (py < 1) {
			return null;
		}
		
		MutableBlockPos mpos = new MutableBlockPos(px & 15, py - 1, pz & 15);
		while (mpos.getY() > 0 && isTerrain(chunk.getBlockState(mpos))) {
			mpos.setY(mpos.getY() - 4);
		}
		
		py -= 12;
		mpos.setY(mpos.getY() + 12);
		
		if (py <= mpos.getY()) {
			return null;
		}
		
		py = MHelper.randRange(mpos.getY(), py, random);
		return new BlockPos(px, py, pz);
	}
	
	private boolean isReplaceable(BlockState state) {
		if (isTerrain(state)) {
			return true;
		}
		Material material = state.getMaterial();
		return material.isReplaceable() || material.equals(Material.PLANT) || material.equals(Material.LEAVES);
	}
}
