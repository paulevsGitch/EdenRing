package paulevs.edenring.registries;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.particles.LeafParticle;
import paulevs.edenring.particles.WindParticle;

public class EdenParticles {
	public static final SimpleParticleType AURITIS_LEAF_PARTICLE = register("auritis_leaf_particle");
	public static final SimpleParticleType WIND_PARTICLE = register("wind_particle");
	
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(AURITIS_LEAF_PARTICLE, LeafParticle.ParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(WIND_PARTICLE, WindParticle.ParticleFactory::new);
	}
	
	private static SimpleParticleType register(String name) {
		return Registry.register(Registry.PARTICLE_TYPE, EdenRing.makeID(name), FabricParticleTypes.simple());
	}
}
