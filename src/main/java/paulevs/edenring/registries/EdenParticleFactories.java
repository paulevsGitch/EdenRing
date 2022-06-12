package paulevs.edenring.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import paulevs.edenring.particles.LeafParticle;
import paulevs.edenring.particles.OscillatingParticle;
import paulevs.edenring.particles.WindParticle;

@Environment(EnvType.CLIENT)
public class EdenParticleFactories {
	public static void register() {
		ParticleFactoryRegistry.getInstance().register(EdenParticles.AURITIS_LEAF_PARTICLE, LeafParticle.ParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(EdenParticles.WIND_PARTICLE, WindParticle.ParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(EdenParticles.YOUNG_VOLVOX, OscillatingParticle.ParticleFactory::new);
	}
}
