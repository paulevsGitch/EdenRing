package paulevs.edenring.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import ru.bclib.util.MHelper;

public class LeafParticle extends TextureSheetParticle {
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;
	
	protected LeafParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
		super(world, x, y, z, r, g, b);
		pickSprite(sprites);
		
		this.lifetime = MHelper.randRange(120, 200, random);
		this.quadSize = MHelper.randRange(0.15F, 0.25F, random);
		this.setAlpha(0);
		
		preVX = 0;
		preVY = 0;
		preVZ = 0;
		
		nextVX = random.nextGaussian() * 0.02;
		nextVY = -random.nextDouble() * 0.02 - 0.02;
		nextVZ = random.nextGaussian() * 0.02;
	}
	
	@Override
	public void tick() {
		int ticks = this.age & 63;
		if (ticks == 0) {
			preVX = nextVX;
			preVY = nextVY;
			preVZ = nextVZ;
			nextVX = random.nextGaussian() * 0.02;
			nextVY = -random.nextDouble() * 0.02 - 0.02;
			nextVZ = random.nextGaussian() * 0.02;
		}
		double delta = (double) ticks / 63.0;
		
		if (this.age <= 40) {
			this.setAlpha(this.age / 40F);
		}
		else if (this.age >= this.lifetime - 40) {
			this.setAlpha((this.lifetime - this.age) / 40F);
		}
		
		if (this.age >= this.lifetime) {
			this.remove();
		}
		
		this.xd = Mth.lerp(delta, preVX, nextVX);
		this.yd = Mth.lerp(delta, preVY, nextVY);
		this.zd = Mth.lerp(delta, preVZ, nextVZ);
		
		super.tick();
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	@Environment(EnvType.CLIENT)
	public static class ParticleFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;
		
		public ParticleFactory(SpriteSet sprites) {
			this.sprites = sprites;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double vX, double vY, double vZ) {
			return new LeafParticle(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}
