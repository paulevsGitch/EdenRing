package paulevs.edenring.world.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import ru.bclib.world.generator.BiomeMap;
import ru.bclib.world.generator.BiomePicker;

import java.util.Map;
import java.util.stream.Collectors;

public class EdenBiomeSource extends BiomeSource {
	public static final Codec<EdenBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		})).apply(instance, instance.stable(EdenBiomeSource::new));
	});
	
	private final Registry<Biome> biomeRegistry;
	private static BiomePicker picker;
	private static BiomeMap map;
	
	public EdenBiomeSource(Registry<Biome> biomeRegistry) {
		super(biomeRegistry
			.entrySet()
			.stream()
			.filter(entry -> entry.getKey().location().getNamespace().equals(EdenRing.MOD_ID))
			.map(Map.Entry::getValue)
			.collect(Collectors.toList()));
		this.biomeRegistry = biomeRegistry;
		
		if (picker == null) {
			picker = new BiomePicker();
			EdenBiomes.BIOMES.forEach(biome -> picker.addBiome(biome));
			picker.rebuild();
		}
		picker.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		map = new BiomeMap(TerrainGenerator.seed, GeneratorOptions.biomeSize, picker);
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
	public Biome getNoiseBiome(int x, int y, int z) {
		if (map.getSeed() != TerrainGenerator.seed) {
			map = new BiomeMap(TerrainGenerator.seed, GeneratorOptions.biomeSize, picker);
		}
		if ((x & 63) == 0 && (z & 63) == 0) {
			map.clearCache();
		}
		return map.getBiome(x, z).getActualBiome();
	}
}
