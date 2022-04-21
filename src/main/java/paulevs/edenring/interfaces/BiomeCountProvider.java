package paulevs.edenring.interfaces;

import ru.bclib.world.biomes.BCLBiome;

import java.util.Random;

@FunctionalInterface
public interface BiomeCountProvider {
	int getCount(BCLBiome biome, Random random);
}
