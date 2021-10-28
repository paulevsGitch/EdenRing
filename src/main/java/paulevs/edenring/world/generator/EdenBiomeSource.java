package paulevs.edenring.world.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import paulevs.edenring.EdenRing;
import ru.bclib.api.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
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
			this.possibleBiomes.forEach(biome -> {
				BCLBiome bclBiome = BiomeAPI.getBiome(biomeRegistry.getKey(biome));
				picker.addBiome(bclBiome);
			});
			picker.rebuild();
		}
		picker.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		map = new BiomeMap(0, GeneratorOptions.biomeSize, picker);
	}
	
	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}
	
	@Override
	public BiomeSource withSeed(long seed) {
		EdenBiomeSource source = new EdenBiomeSource(biomeRegistry);
		map = new BiomeMap(seed, GeneratorOptions.biomeSize, picker);
		return source;
	}
	
	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return map.getBiome(x, z).getActualBiome();
	}
}
