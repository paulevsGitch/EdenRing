package paulevs.edenring.client.environment;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TransformHelper {
	private static final Vector3f p1 = new Vector3f();
	private static final Vector3f p2 = new Vector3f();
	
	public static void applyPerspective(PoseStack poseStack, Camera camera) {
		poseStack.mulPose(camera.rotation());
	}
	
	public static void fixBobbing(PoseStack poseStack, Player player, float tickDelta) {
		float g = player.walkDist - player.walkDistO;
		float h = -(player.walkDist + g * tickDelta);
		float i = Mth.lerp(tickDelta, player.oBob, player.bob);
		poseStack.translate(Mth.sin(h * (float)Math.PI) * i * 0.5f, -Math.abs(Mth.cos(h * (float)Math.PI) * i), 0.0);
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(h * (float)Math.PI) * i * 3.0f));
		poseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos(h * (float)Math.PI - 0.2f) * i) * 5.0f));
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

		poseStack.mulPose(fromXYZ(0, yRot, 0));
		poseStack.mulPose(fromXYZ(xRot, 0, 0));
	}

	private static Quaternionf fromXYZ(float x, float y, float z) {
		Quaternionf quaternion = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
		quaternion.mul(new Quaternionf((float)Math.sin(x / 2.0F), 0.0F, 0.0F, (float)Math.cos(x / 2.0F)));
		quaternion.mul(new Quaternionf(0.0F, (float)Math.sin((y / 2.0F)), 0.0F, (float)Math.cos(y / 2.0F)));
		quaternion.mul(new Quaternionf(0.0F, 0.0F, (float)Math.sin(z / 2.0F), (float)Math.cos(z / 2.0F)));
		return quaternion;
	}
}
