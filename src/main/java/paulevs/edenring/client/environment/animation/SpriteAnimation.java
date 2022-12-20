package paulevs.edenring.client.environment.animation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public abstract class SpriteAnimation {
	public abstract void update(double time);
	public abstract float getScale();
	public abstract float getAlpha();
	public abstract byte getFrame();
	public abstract boolean useFogColor();
	public abstract float getVSize();
	
	protected final BlockPos origin;
	
	public SpriteAnimation(BlockPos origin) {
		this.origin = origin;
	}
	
	@Nullable
	public AABB getBoundingBox() {
		return null;
	}
	
	public BlockPos getOrigin() {
		return origin;
	}
	
	public void offset(Vector3f pos) {}
	
	public float fogStartMultiplier() {
		return 1.0F;
	}
	
	public float fogEndMultiplier() {
		return 1.0F;
	}
	
	public boolean useSqrSqr() {
		return false;
	}
}
