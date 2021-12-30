package paulevs.edenring.world.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.world.features.caves.EndCaveFeature;
import ru.bclib.interfaces.BiomeMap;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomePicker;
import ru.bclib.world.generator.map.hex.HexBiomeMap;

import java.util.Map;
import java.util.stream.Collectors;

public class EdenBiomeSource extends BiomeSource {
	public static final Codec<EdenBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		})).apply(instance, instance.stable(EdenBiomeSource::new));
	});
	
	private final Registry<Biome> biomeRegistry;
	private static BiomePicker pickerLand;
	private static BiomePicker pickerCave;
	private static BiomeMap mapLand;
	private static BiomeMap mapCave;
	private static long lastSeed;
	
	public EdenBiomeSource(Registry<Biome> biomeRegistry) {
		super(biomeRegistry
			.entrySet()
			.stream()
			.filter(entry -> entry.getKey().location().getNamespace().equals(EdenRing.MOD_ID))
			.map(Map.Entry::getValue)
			.collect(Collectors.toList()));
		this.biomeRegistry = biomeRegistry;
		
		if (pickerLand == null) {
			pickerLand = new BiomePicker();
			EdenBiomes.BIOMES_LAND.forEach(biome -> pickerLand.addBiome(biome));
			pickerLand.rebuild();
			
			pickerCave = new BiomePicker();
			EdenBiomes.BIOMES_CAVE.forEach(biome -> pickerCave.addBiome(biome));
			pickerCave.rebuild();
		}
		
		lastSeed = TerrainGenerator.seed;
		
		pickerLand.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		pickerCave.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		
		mapLand = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeLand, pickerLand);
		mapCave = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeCave, pickerCave);
		
		EndCaveFeature.BIOME_SOURCE = this;
	}
	
	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}
	
	@Override
	public BiomeSource withSeed(long seed) {
		return new EdenBiomeSource(biomeRegistry);
	}
	
	@Override
	public Biome getNoiseBiome(int x, int y, int z, Sampler sampler) {
		return getLandBiome(x, z).getActualBiome();
	}
	
	public BCLBiome getCaveBiome(int x, int z) {
		return mapCave.getBiome(x << 2, 0, z << 2);
	}
	
	public BCLBiome getLandBiome(int x, int z) {
		if (lastSeed != TerrainGenerator.seed) {
			mapLand = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeLand, pickerLand);
			mapCave = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeCave, pickerCave);
			lastSeed = TerrainGenerator.seed;
		}
		if ((x & 63) == 0 && (z & 63) == 0) {
			mapLand.clearCache();
			mapCave.clearCache();
		}
		return mapLand.getBiome(x << 2, 0, z << 2);
	}
}
