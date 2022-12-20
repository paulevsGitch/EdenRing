package paulevs.edenring.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import paulevs.edenring.entities.LightningRayEntity;

@Environment(value= EnvType.CLIENT)
public class LightningRayRenderer extends EntityRenderer<LightningRayEntity> {
	private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");
	private static final RenderType BEAM_RENDER_TYPE = RenderType.entityTranslucent(GUARDIAN_BEAM_LOCATION, true);
	private static final Matrix3f NORMAL = new Matrix3f();
	private static final Vec3 SIDE = new Vec3(1, 0, 0);
	private static final Vec3 UP = new Vec3(0, 1, 0);
	private static final int COLOR = 15728880;
	
	public LightningRayRenderer(Context context) {
		super(context);
		this.shadowRadius = 0.0F;
	}
	
	@Override
	public ResourceLocation getTextureLocation(LightningRayEntity entity) {
		return GUARDIAN_BEAM_LOCATION;
	}
	
	@Override
	protected int getSkyLightLevel(LightningRayEntity entity, BlockPos blockPos) {
		return 15;
	}
	
	@Override
	protected int getBlockLightLevel(LightningRayEntity entity, BlockPos blockPos) {
		return 15;
	}
	
	@Override
	public void render(LightningRayEntity entity, float f, float deltaTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
		if (entity.getDir() == null) {
			return;
		}
		
		Vec3 axis = entity.getDir();
		if (axis.x == 0 && axis.y == 0 && axis.z == 0) {
			return;
		}
		
		float time = -(entity.tickCount + deltaTick) * 0.75F;
		float dv = (float) axis.length() + time;
		Vec3 side = axis.y == 1 || axis.y == -1 ? SIDE : axis.cross(UP).normalize();
		float x1 = (float) side.x * 0.25F;
		float y1 = (float) side.y * 0.25F;
		float z1 = (float) side.z * 0.25F;
		float x2 = (float) axis.x - x1;
		float y2 = (float) axis.y - y1;
		float z2 = (float) axis.z - z1;
		float x3 = (float) axis.x + x1;
		float y3 = (float) axis.y + y1;
		float z3 = (float) axis.z + z1;
		
		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(BEAM_RENDER_TYPE);//multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
		poseStack.pushPose();
		Pose lastPose = poseStack.last();
		Matrix4f pose = lastPose.pose();
		Matrix3f normal = lastPose.normal();
		vertex(vertexConsumer, pose, normal, -x1, y1, -z1, 0.0F, time);
		vertex(vertexConsumer, pose, normal,  x2, y2,  z2, 0.0F,   dv);
		vertex(vertexConsumer, pose, normal,  x3, y3,  z3, 0.5F,   dv);
		vertex(vertexConsumer, pose, normal,  x1, y1,  z1, 0.5F, time);
		poseStack.popPose();
		
		super.render(entity, f, deltaTick, poseStack, multiBufferSource, i);
	}
	
	private void vertex(VertexConsumer vertexConsumer, Matrix4f pose, Matrix3f normal, float x, float y, float z, float u, float v) {
		vertexConsumer
			.vertex(pose, x, y, z)
			.color(255, 255, 255, 255)
			.uv(u, v)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(COLOR)
			.normal(normal, 0, 1, 0)
			.endVertex();
	}
}
