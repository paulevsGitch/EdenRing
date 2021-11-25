package paulevs.edenring.registries;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.particles.LeafParticle;
import paulevs.edenring.particles.WindParticle;

public class EdenParticles {
	public static final SimpleParticleType AURITIS_LEAF_PARTICLE = register("auritis_leaf_particle", LeafParticle.ParticleFactory::new);
	public static final SimpleParticleType WIND_PARTICLE = register("wind_particle", WindParticle.ParticleFactory::new);
	
	public static void init() {}
	
	private static SimpleParticleType register(String name, PendingParticleFactory<SimpleParticleType> factory) {
		SimpleParticleType particles = Registry.register(Registry.PARTICLE_TYPE, EdenRing.makeID(name), FabricParticleTypes.simple());
		ParticleFactoryRegistry.getInstance().register(particles, factory);
		return particles;
	}
}
