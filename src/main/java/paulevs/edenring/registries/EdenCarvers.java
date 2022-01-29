package paulevs.edenring.registries;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.EdenCaveCarver;

public class EdenCarvers {
	public static final WorldCarver<CaveCarverConfiguration> CAVE = register("cave", new EdenCaveCarver(CaveCarverConfiguration.CODEC));
	public static final ConfiguredWorldCarver<CaveCarverConfiguration> CAVE_CONFIGURED = register("cave", CAVE.configured(
		new CaveCarverConfiguration(
			1F,
			UniformHeight.of(VerticalAnchor.bottom(), VerticalAnchor.top()),
			ConstantFloat.of(1.0F),
			VerticalAnchor.bottom(),
			CarverDebugSettings.of(false, Blocks.ACACIA_BUTTON.defaultBlockState()),
			ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), UniformFloat.of(-1.0f, -0.4f)
		)
	));
	
	private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String string, F worldCarver) {
		return (F) Registry.register(Registry.CARVER, EdenRing.makeID(string), worldCarver);
	}
	
	private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(String string, ConfiguredWorldCarver<WC> configuredWorldCarver) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, EdenRing.makeID(string), configuredWorldCarver);
	}
}
