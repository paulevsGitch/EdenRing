package paulevs.edenring.registries;

import com.google.common.collect.Lists;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.biomes.BrainstormBiome;
import paulevs.edenring.world.biomes.GoldenForestBiome;
import paulevs.edenring.world.biomes.LakesideDesertBiome;
import paulevs.edenring.world.biomes.MycoticForestBiome;
import paulevs.edenring.world.biomes.PulseForestBiome;
import paulevs.edenring.world.biomes.StoneGardenBiome;
import ru.bclib.api.BiomeAPI;
import ru.bclib.config.EntryConfig;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

import java.util.List;

public class EdenBiomes {
	private static final IdConfig CONFIG = new EntryConfig(EdenRing.MOD_ID, "biomes");
	public static final List<BCLBiome> BIOMES = Lists.newArrayList();
	
	private static final SurfaceBuilderBaseConfiguration CONFIG_GRASS = new SurfaceBuilderBaseConfiguration(
		EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState(),
		Blocks.DIRT.defaultBlockState(),
		Blocks.DIRT.defaultBlockState()
	);
	private static final SurfaceBuilderBaseConfiguration CONFIG_MYCELIUM = new SurfaceBuilderBaseConfiguration(
		EdenBlocks.EDEN_MYCELIUM.defaultBlockState(),
		Blocks.DIRT.defaultBlockState(),
		Blocks.DIRT.defaultBlockState()
	);
	private static final ConfiguredSurfaceBuilder DEFAULT_BUILDER = registerSurf("default_grass", SurfaceBuilder.DEFAULT.configured(CONFIG_GRASS));
	private static final ConfiguredSurfaceBuilder MYCELIUM_BUILDER = registerSurf("mycelium", SurfaceBuilder.DEFAULT.configured(CONFIG_MYCELIUM));
	
	public static final BCLBiome STONE_GARDEN = register(new StoneGardenBiome(CONFIG, DEFAULT_BUILDER));
	public static final BCLBiome GOLDEN_FOREST = register(new GoldenForestBiome(CONFIG, DEFAULT_BUILDER));
	public static final BCLBiome MYCOTIC_FOREST = register(new MycoticForestBiome(CONFIG, MYCELIUM_BUILDER));
	public static final BCLBiome PULSE_FOREST = register(new PulseForestBiome(CONFIG, DEFAULT_BUILDER));
	public static final BCLBiome BRAINSTORM = register(new BrainstormBiome(CONFIG, DEFAULT_BUILDER));
	public static final BCLBiome LAKESIDE_DESERT = register(new LakesideDesertBiome(CONFIG, SurfaceBuilders.DESERT));
	
	public static void init() {
		CONFIG.saveChanges();
	}
	
	private static BCLBiome register(BCLBiome biome) {
		BIOMES.add(biome);
		return BiomeAPI.registerBiome(biome);
	}
	
	public static BCLBiomeDef addDefaultFeatures(BCLBiomeDef def) {
		def.addFeature(EdenFeatures.SLATE_LAYER);
		def.addFeature(EdenFeatures.CALCITE_LAYER);
		def.addFeature(EdenFeatures.TUFF_LAYER);
		def.addFeature(Decoration.UNDERGROUND_DECORATION, Features.ORE_GRANITE);
		def.addFeature(Decoration.UNDERGROUND_DECORATION, Features.ORE_ANDESITE);
		def.addFeature(Decoration.UNDERGROUND_DECORATION, Features.ORE_DIORITE);
		def.addFeature(EdenFeatures.ORE_MOSSY_COBBLE);
		def.addFeature(EdenFeatures.ORE_COBBLE);
		def.addFeature(EdenFeatures.ORE_COAL);
		def.addFeature(EdenFeatures.ORE_IRON);
		def.addFeature(EdenFeatures.ORE_COPPER);
		def.addFeature(EdenFeatures.GRAVILITE_CRYSTAL);
		def.addCarver(Carving.AIR, Carvers.CAVE);
		return def;
	}
	
	private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> registerSurf(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, EdenRing.makeID(name), configuredSurfaceBuilder);
	}
}
