package paulevs.edenring.client.environment.clouds;

import net.minecraft.core.BlockPos;

public class CloudAnimation {
	private static final int SPEED = 2000;
	private static final int SPEED2 = SPEED - 300;
	
	private final BlockPos origin;
	private final byte index;
	private final float size;
	private final int start;
	private float offset;
	private float scale;
	private float alpha;
	
	public CloudAnimation(BlockPos origin, int start, byte index, float size) {
		this.origin = origin;
		this.start = start;
		this.index = index;
		this.size = size;
	}
	
	public void update(double time) {
		float state = (float) ((time + start) % SPEED) / SPEED2;
		if (state > 1) {
			scale = 0;
			alpha = 0;
			return;
		}
		offset = (0.5F - state) * 100.0F;
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
}
