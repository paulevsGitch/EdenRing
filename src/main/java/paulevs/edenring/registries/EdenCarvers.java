package paulevs.edenring.registries;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import paulevs.edenring.EdenRing;

public class EdenCarvers {
	private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String string, F worldCarver) {
		return (F) Registry.register(Registry.CARVER, EdenRing.makeID(string), worldCarver);
	}
	
	private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(String string, ConfiguredWorldCarver<WC> configuredWorldCarver) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_CARVER, EdenRing.makeID(string), configuredWorldCarver);
	}
}
