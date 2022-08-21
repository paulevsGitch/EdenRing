package paulevs.edenring.registries;

import com.google.common.collect.Lists;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI.BiomeType;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.biomes.CaveBiomes;
import paulevs.edenring.world.biomes.LandBiomes;
import paulevs.edenring.world.biomes.VoidBiomes;

import java.util.List;

public class EdenBiomes {
	private static final IdConfig CONFIG = new EntryConfig(EdenRing.MOD_ID, "biomes");
	public static final List<BCLBiome> BIOMES_LAND = Lists.newArrayList();
	public static final List<BCLBiome> BIOMES_VOID = Lists.newArrayList();
	public static final List<BCLBiome> BIOMES_CAVE = Lists.newArrayList();
	
	// LAND //
	public static final BCLBiome STONE_GARDEN = registerLand(LandBiomes.makeStoneGardenBiome());
	public static final BCLBiome GOLDEN_FOREST = registerLand(LandBiomes.makeGoldenForestBiome());
	public static final BCLBiome MYCOTIC_FOREST = registerLand(LandBiomes.makeMycoticForestBiome());
	public static final BCLBiome PULSE_FOREST = registerLand(LandBiomes.makePulseForestBiome());
	public static final BCLBiome BRAINSTORM = registerLand(LandBiomes.makeBrainstormBiome());
	public static final BCLBiome LAKESIDE_DESERT = registerLand(LandBiomes.makeLakesideDesertBiome());
	public static final BCLBiome WIND_VALLEY = registerLand(LandBiomes.makeWindValleyBiome());
	
	// VOID //
	public static final BCLBiome AIR_OCEAN = registerVoid(VoidBiomes.makeAirOcean());
	public static final BCLBiome SKY_COLONY = registerVoid(VoidBiomes.makeSkyColony());
	
	// CAVES //
	public static final BCLBiome EMPTY_CAVE = registerCave(CaveBiomes.makeEmptyCaveBiome());
	public static final BCLBiome ERODED_CAVE = registerCave(CaveBiomes.makeErodedCaveBiome());
	
	// SUBBIOMES //
	public static final BCLBiome OLD_MYCOTIC_FOREST = registerSubLand(MYCOTIC_FOREST, LandBiomes.makeOldMycoticForestBiome());
	
	public static void init() {
		CONFIG.saveChanges();
	}
	
	private static BCLBiome registerLand(BCLBiome biome) {
		BIOMES_LAND.add(biome);
		return BiomeAPI.registerBuiltinBiomeAndOverrideIntendedDimension(biome, BiomeType.NONE);
	}
	
	private static BCLBiome registerVoid(BCLBiome biome) {
		BIOMES_VOID.add(biome);
		return BiomeAPI.registerBuiltinBiomeAndOverrideIntendedDimension(biome, BiomeType.NONE);
	}
	
	private static BCLBiome registerCave(BCLBiome biome) {
		BIOMES_CAVE.add(biome);
		return BiomeAPI.registerBuiltinBiomeAndOverrideIntendedDimension(biome, BiomeType.NONE);
	}
	
	private static BCLBiome registerSubLand(BCLBiome parent, BCLBiome biome) {
		return BiomeAPI.registerSubBiome(parent, biome);
	}
}
