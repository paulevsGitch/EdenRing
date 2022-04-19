package paulevs.edenring.client.environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;

public class TransformHelper {
	private static final Vector3f p1 = new Vector3f();
	private static final Vector3f p2 = new Vector3f();
	
	public static void applyPerspective(PoseStack poseStack, Camera camera) {
		poseStack.mulPose(camera.rotation());
	}
	
	public static void translateAndRotate(double posX, double posY, double posZ, PoseStack poseStack, Camera camera) {
		double dx = posX - camera.getPosition().x;
		double dy = posY - camera.getPosition().y;
		double dz = posZ - camera.getPosition().z;
		translateAndRotateRelative(dx, dy, dz, poseStack);
	}
	
	public static void translateAndRotateRelative(double dx, double dy, double dz, PoseStack poseStack) {
		poseStack.translate(-dx, dy, -dz);
		
		p1.set((float) dx, (float) dy, (float) dz);
		p2.set((float) dx, 0, (float) dz);
		p1.normalize();
		p2.normalize();
		float xRot = (float) Math.acos(p1.dot(p2)) * Mth.sign(dy);
		float yRot = (float) Math.atan2(p1.x(), p1.z());
		
		poseStack.mulPose(Quaternion.fromXYZ(0, yRot, 0));
		poseStack.mulPose(Quaternion.fromXYZ(xRot, 0, 0));
	}
}
