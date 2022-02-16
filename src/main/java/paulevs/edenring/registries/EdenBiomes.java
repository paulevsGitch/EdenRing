package paulevs.edenring.registries;

import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.biomes.LandBiomes;
import paulevs.edenring.world.biomes.VoidBiomes;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.config.EntryConfig;
import ru.bclib.config.IdConfig;
import ru.bclib.mixin.common.BiomeGenerationSettingsAccessor;
import ru.bclib.util.CollectionsUtil;
import ru.bclib.world.biomes.BCLBiome;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

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
	//public static final BCLBiome ERODED_CAVE = registerCave(makeStoneGardenBiome());
	
	public static void init() {
		CONFIG.saveChanges();
		// TODO remove fix when it will be implemented in BCLib
		//BIOMES_LAND.forEach(biome -> addStepFeaturesToBiome(biome));
		//BIOMES_VOID.forEach(biome -> addStepFeaturesToBiome(biome));
		//BIOMES_CAVE.forEach(biome -> addStepFeaturesToBiome(biome));
	}
	
	private static BCLBiome registerLand(BCLBiome biome) {
		BIOMES_LAND.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	private static BCLBiome registerVoid(BCLBiome biome) {
		BIOMES_VOID.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	private static BCLBiome registerCave(BCLBiome biome) {
		BIOMES_CAVE.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	// TODO remove fix when it will be implemented in BCLib
	private static void addStepFeaturesToBiome(BCLBiome biome) {
		Map<Decoration, List<Supplier<PlacedFeature>>> featureMap = biome.getFeatures();
		BiomeGenerationSettingsAccessor accessor = (BiomeGenerationSettingsAccessor) biome.getBiome().getGenerationSettings();
		List<List<Supplier<PlacedFeature>>> allFeatures = CollectionsUtil.getMutable(accessor.bclib_getFeatures());
		Set<PlacedFeature> set = CollectionsUtil.getMutable(accessor.bclib_getFeatureSet());
		
		for (Decoration step: featureMap.keySet()) {
			List<Supplier<PlacedFeature>> features = getFeaturesList(allFeatures, step);
			List<Supplier<PlacedFeature>> featureList = featureMap.get(step);
			
			for (Supplier<PlacedFeature> feature : featureList) {
				features.add(feature);
				set.add(feature.get());
			}
		}
		accessor.bclib_setFeatures(allFeatures);
		accessor.bclib_setFeatureSet(set);
	}
	
	// TODO remove fix when it will be implemented in BCLib
	private static List<Supplier<PlacedFeature>> getFeaturesList(List<List<Supplier<PlacedFeature>>> features, Decoration step) {
		int index = step.ordinal();
		while (features.size() <= index) {
			features.add(Lists.newArrayList());
		}
		List<Supplier<PlacedFeature>> mutable = CollectionsUtil.getMutable(features.get(index));
		features.set(index, mutable);
		return mutable;
	}
}
