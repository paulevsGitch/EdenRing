package paulevs.edenring.world.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.ChunkPos;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class EdenBiomeSource extends BiomeSource {
	public static final Codec<EdenBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		})).apply(instance, instance.stable(EdenBiomeSource::new));
	});
	
	private final Map<ChunkPos, LandCache> landCache = new ConcurrentHashMap<>();
	//private final Map<ChunkPos, Boolean> landCache = new ConcurrentHashMap<>();
	private final double[] terrainData = new double[16];
	private final Registry<Biome> biomeRegistry;
	private BiomePicker pickerLand;
	private BiomePicker pickerVoid;
	private BiomePicker pickerCave;
	private BiomeMap mapLand;
	private BiomeMap mapVoid;
	private BiomeMap mapCave;
	private long lastSeed;
	
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
			
			pickerVoid = new BiomePicker();
			EdenBiomes.BIOMES_VOID.forEach(biome -> pickerVoid.addBiome(biome));
			pickerVoid.rebuild();
			
			pickerCave = new BiomePicker();
			EdenBiomes.BIOMES_CAVE.forEach(biome -> pickerCave.addBiome(biome));
			pickerCave.rebuild();
		}
		
		lastSeed = TerrainGenerator.seed;
		
		pickerLand.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		pickerVoid.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		pickerCave.getBiomes().forEach(biome -> biome.updateActualBiomes(biomeRegistry));
		
		mapLand = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeLand, pickerLand);
		mapVoid = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeVoid, pickerVoid);
		mapCave = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeCave, pickerCave);
		
		EndCaveFeature.BIOME_SOURCE = this;
		landCache.clear();
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
		cleanCache(x, z);
		if (isLand(x, z)) {
			return getLandBiome(x, z).getActualBiome();
		}
		return getVoidBiome(x, z).getActualBiome();
	}
	
	public BCLBiome getCaveBiome(int x, int z) {
		checkSeed();
		return mapCave.getBiome(x << 2, 0, z << 2);
	}
	
	public BCLBiome getLandBiome(int x, int z) {
		checkSeed();
		return mapLand.getBiome(x << 2, 0, z << 2);
	}
	
	public BCLBiome getVoidBiome(int x, int z) {
		checkSeed();
		return mapVoid.getBiome(x << 2, 0, z << 2);
	}
	
	private void checkSeed() {
		if (lastSeed != TerrainGenerator.seed) {
			lastSeed = TerrainGenerator.seed;
			mapLand = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeLand, pickerLand);
			mapVoid = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeVoid, pickerVoid);
			mapCave = new HexBiomeMap(lastSeed, GeneratorOptions.biomeSizeCave, pickerCave);
			landCache.clear();
		}
	}
	
	private void cleanCache(int x, int z) {
		if ((x & 63) == 0 && (z & 63) == 0) {
			mapLand.clearCache();
			mapVoid.clearCache();
			mapCave.clearCache();
		}
	}
	
	private boolean isLand(int x, int z) {
		boolean result = false;
		TerrainGenerator.lock();
		TerrainGenerator.fillTerrainDensity(terrainData, x << 2 | 2, z << 2 | 2, 4, 16);
		for (byte py = 0; py < 16; py++) {
			if (terrainData[py] > -0.2F) {
				result = true;
				break;
			}
		}
		TerrainGenerator.unlock();
		return result;
		
		/*final ChunkPos pos = new ChunkPos(LandCache.toChunk(x), LandCache.toChunk(z));
		if (landCache.size() > 4094) {
			System.out.println("Clear");
			landCache.clear();
		}
		return landCache.computeIfAbsent(pos, n -> {
			LandCache cache = new LandCache();
			int sx = LandCache.fromChunk(pos.x);
			int sz = LandCache.fromChunk(pos.z);
			TerrainGenerator.lock();
			for (byte i = 0; i < LandCache.getSide(); i++) {
				int px = (sx | i) << 2 | 2;
				for (byte j = 0; j < LandCache.getSide(); j++) {
					int pz = (sz | j) << 2 | 2;
					TerrainGenerator.fillTerrainDensity(terrainData, px, pz, 4, 16);
					for (byte py = 0; py < 16; py++) {
						if (terrainData[py] > -0.2F) {
							cache.setData(i, j, true);
							break;
						}
					}
				}
			}
			TerrainGenerator.unlock();
			System.out.println("Cache for " + pos);
			return cache;
		}).getData(LandCache.wrap(x), LandCache.wrap(z));*/
	}
}
