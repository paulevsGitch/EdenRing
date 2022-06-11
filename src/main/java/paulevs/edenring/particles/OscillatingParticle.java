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
import org.betterx.bclib.util.MHelper;

public class OscillatingParticle extends TextureSheetParticle {
	private double preVX;
	private double preVY;
	private double preVZ;
	private double nextVX;
	private double nextVY;
	private double nextVZ;
	private float size;
	
	protected OscillatingParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
		super(world, x, y, z, r, g, b);
		pickSprite(sprites);
		
		this.lifetime = MHelper.randRange(200, 400, random);
		this.quadSize = MHelper.randRange(0.15F, 0.25F, random);
		this.setAlpha(0);
		
		size = MHelper.randRange(0.125F, 0.25F, random);
		quadSize = 0;
		setNext();
	}
	
	@Override
	public void tick() {
		int ticks = age & 63;
		if (ticks == 0) {
			copyPre();
			setNext();
		}
		double delta = (double) ticks / 63.0;
		
		if (this.age <= 80) {
			float d = age / 80F;
			setAlpha(d);
			quadSize = size * d;
		}
		else if (age >= lifetime - 80) {
			float d = (lifetime - age) / 80F;
			this.setAlpha(d);
			quadSize = size * d;
		}
		
		this.xd = Mth.lerp(delta, preVX, nextVX);
		this.yd = Mth.lerp(delta, preVY, nextVY);
		this.zd = Mth.lerp(delta, preVZ, nextVZ);
		
		super.tick();
	}
	
	private void setNext() {
		nextVX = random.nextGaussian() * 0.01;
		nextVY = random.nextGaussian() * 0.01;
		nextVZ = random.nextGaussian() * 0.01;
	}
	
	private void copyPre() {
		preVX = nextVX;
		preVY = nextVY;
		preVZ = nextVZ;
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
			return new OscillatingParticle(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}
