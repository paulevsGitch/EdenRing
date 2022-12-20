package paulevs.edenring.registries;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import paulevs.edenring.EdenRing;

public class EdenParticles {
	public static final SimpleParticleType AURITIS_LEAF_PARTICLE = register("auritis_leaf_particle");
	public static final SimpleParticleType WIND_PARTICLE = register("wind_particle");
	public static final SimpleParticleType YOUNG_VOLVOX = register("young_volvox");
	
	private static SimpleParticleType register(String name) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, EdenRing.makeID(name), FabricParticleTypes.simple());
	}
}
