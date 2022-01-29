package paulevs.edenring.world.biomes;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.EdenCaveCarver;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.surface.SurfaceRuleBuilder;

public class BiomesCommonMethods {
	public static final WorldCarver<CaveCarverConfiguration> CAVE = register("cave", new EdenCaveCarver(CaveCarverConfiguration.CODEC));
	public static final ConfiguredWorldCarver<CaveCarverConfiguration> CAVE_CONFIGURED = register("cave",
		CAVE.configured(
			new CaveCarverConfiguration(
				1F,
				UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top()),
				ConstantFloat.of(1.0F),
				VerticalAnchor.bottom(),
				CarverDebugSettings.of(false, Blocks.ACACIA_BUTTON.defaultBlockState()),
				ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), UniformFloat.of(-1.0f, -0.4f)
			)
		)
	);
	
	private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String string, F worldCarver) {
		return (F) Registry.register(Registry.CARVER, EdenRing.makeID(string), worldCarver);
	}
	
	private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(String string, ConfiguredWorldCarver<WC> configuredWorldCarver) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, EdenRing.makeID(string), configuredWorldCarver);
	}
	
	public static void addCaves(BCLBiomeBuilder builder) {
		builder.carver(Carving.AIR, CAVE_CONFIGURED);
		//builder.feature(EdenFeatures.SDF_CAVE);
	}
	
	public static void addDefaultFeatures(BCLBiomeBuilder builder) {
		builder
			.feature(EdenFeatures.SLATE_LAYER)
			.feature(EdenFeatures.CALCITE_LAYER)
			.feature(EdenFeatures.TUFF_LAYER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_GRANITE_UPPER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANDESITE_UPPER)
			.feature(Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_DIORITE_UPPER)
			.feature(EdenFeatures.SMALL_ISLAND)
			.feature(EdenFeatures.ORE_MOSSY_COBBLE)
			.feature(EdenFeatures.ORE_COBBLE)
			.feature(EdenFeatures.ORE_COAL)
			.feature(EdenFeatures.ORE_IRON)
			.feature(EdenFeatures.ORE_COPPER)
			.feature(EdenFeatures.GRAVILITE_CRYSTAL);
	}
	
	public static void addDefaultSurface(BCLBiomeBuilder builder) {
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(EdenBlocks.EDEN_GRASS_BLOCK.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
	}
	
	public static void addDefaultSounds(BCLBiomeBuilder builder) {
		builder.music(EdenSounds.MUSIC_COMMON);
	}
	
	public static void setDefaultColors(BCLBiomeBuilder builder) {
		builder.skyColor(113, 178, 255).fogColor(183, 212, 255).waterFogColor(329011).waterColor(4159204);
	}
}
