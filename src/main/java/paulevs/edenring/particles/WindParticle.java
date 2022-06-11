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
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.betterx.bclib.util.MHelper;

public class WindParticle extends TextureSheetParticle {
	private static final int[] OFFSET = new int[] { 0, 10, 18 };
	private static final int[] COUNT = new int[] { 10, 8, 8 };
	private static final int TOTAL = 10 + 8 + 8;
	private final SpriteSet sprites;
	private final byte type;
	
	protected WindParticle(ClientLevel world, double x, double y, double z, double r, double g, double b, SpriteSet sprites) {
		super(world, x, y, z, r, g, b);
		this.sprites = sprites;
		this.lifetime = MHelper.randRange(20, 30, random);
		this.quadSize = MHelper.randRange(0.5F, 0.75F, random);
		this.type = (byte) random.nextInt(3);
		this.setParticleSpeed(0, 0, 0);
		this.setSprite(sprites.get(OFFSET[type], TOTAL));
		this.setAlpha(random.nextFloat() * 0.5F + 0.5F);
		
		int height = world.getHeight(Types.WORLD_SURFACE, (int) (x + 0.5), (int) (z + 0.5));
		if (y < height || y - 8 > height) {
			this.lifetime = 0;
			this.setAlpha(0);
		}
	}
	
	@Override
	public void tick() {
		if (this.lifetime > 0) {
			int frame = (int) ((float) this.age / (float) this.lifetime * COUNT[type]) + OFFSET[type];
			this.setSprite(sprites.get(frame, TOTAL));
		}
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
			return new WindParticle(world, x, y, z, 1, 1, 1, sprites);
		}
	}
}
