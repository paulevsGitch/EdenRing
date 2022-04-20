package paulevs.edenring.client.environment.clouds;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class CloudAnimation {
	private static final float DISTANCE = 300.0F;
	
	private AABB boundingBox;
	private final BlockPos origin;
	private final byte index;
	private final float size;
	private final int start;
	private final int speed;
	private final int speed2;
	private float offset;
	private float scale;
	private float alpha;
	
	public CloudAnimation(BlockPos origin, int start, byte index, float size, int speed) {
		this.origin = origin;
		this.start = start;
		this.index = index;
		this.size = size;
		this.boundingBox = new AABB(origin).inflate(size + DISTANCE + 1, size, size);
		this.speed = speed;
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
	
	public float getScale() {
		return scale;
	}
	
	public float getOffset() {
		return offset;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public BlockPos getOrigin() {
		return origin;
	}
	
	public byte getIndex() {
		return index;
	}
	
	public AABB getBoundingBox() {
		return boundingBox;
	}
}
