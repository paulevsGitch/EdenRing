package paulevs.edenring.client.environment.weather;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import org.betterx.bclib.util.MHelper;
import org.joml.Vector3f;
import paulevs.edenring.client.environment.animation.SpriteAnimation;

public class CloudAnimation extends SpriteAnimation {
	private static final float DISTANCE = 300.0F;
	
	private AABB boundingBox;
	private final byte index;
	private final float size;
	private final int start;
	private final int speed;
	private final int speed2;
	private float offset;
	private float scale;
	private float alpha;
	
	public CloudAnimation(BlockPos origin, RandomSource random) {
		super(origin);
		this.start = random.nextInt(4096);
		this.index = (byte) random.nextInt(4);
		this.size = MHelper.randRange(10.0F, 20.0F, random);
		this.boundingBox = new AABB(origin).inflate(size + DISTANCE + 1, size, size);
		this.speed = MHelper.randRange(3000, 5000, random);
		this.speed2 = (int) (speed * 0.8F);
	}
	
	public void update(double time) {
		float state = (float) ((time + start) % speed) / speed2;
		if (state > 1) {
			scale = 0;
			alpha = 0;
			return;
		}
		offset = (0.5F - state) * DISTANCE;
		state = Math.abs(state * 2.0F - 1.0F);
		float stateSQ = state * state;
		scale = (1.0F - stateSQ) * size;
		alpha = 1.0F - stateSQ * stateSQ;
	}
	
	@Override
	public float getScale() {
		return scale;
	}
	
	@Override
	public void offset(Vector3f pos) {
		pos.set(pos.x() + offset, pos.y(), pos.z());
	}
	
	@Override
	public float getAlpha() {
		return alpha;
	}
	
	@Override
	public byte getFrame() {
		return index;
	}
	
	@Override
	public boolean useFogColor() {
		return true;
	}
	
	@Override
	public float getVSize() {
		return 0.25F;
	}
	
	@Override
	public AABB getBoundingBox() {
		return boundingBox;
	}
	
	@Override
	public float fogStartMultiplier() {
		return 1.5F;
	}
	
	@Override
	public float fogEndMultiplier() {
		return 2.0F;
	}
}
