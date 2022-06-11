package paulevs.edenring.interfaces;

import net.minecraft.util.RandomSource;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;

@FunctionalInterface
public interface BiomeCountProvider {
	int getCount(BCLBiome biome, RandomSource random);
}
