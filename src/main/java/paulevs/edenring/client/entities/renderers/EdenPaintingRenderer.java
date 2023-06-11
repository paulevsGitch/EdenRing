package paulevs.edenring.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import paulevs.edenring.entities.EdenPainting;
import paulevs.edenring.paintings.PaintingColorProvider;
import paulevs.edenring.paintings.PaintingInfo;

@Environment(value= EnvType.CLIENT)
public class EdenPaintingRenderer extends EntityRenderer<EdenPainting> {
	private static final MutableBlockPos POS = new MutableBlockPos();
	private static final int WHITE = 0xFFFFFF;
	
	public EdenPaintingRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(EdenPainting painting, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
		PaintingInfo info = painting.getPainting();
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f));
		poseStack.scale(0.0625F, 0.0625F, 0.0625F);
		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(getTextureLocation(painting)));
		this.renderPainting(poseStack, vertexConsumer, painting, info);
		poseStack.popPose();
		super.render(painting, f, g, poseStack, multiBufferSource, i);
	}
	
	@Override
	public ResourceLocation getTextureLocation(EdenPainting painting) {
		return painting.getPainting().getTexture();
	}
	
	private void renderPainting(PoseStack poseStack, VertexConsumer vertexConsumer, EdenPainting painting, PaintingInfo info) {
		PoseStack.Pose pose = poseStack.last();
		Matrix3f normal = pose.normal();
		Matrix4f pos = pose.pose();
		
		//float f = -info.getWidth() * 0.5F;
		//float g = -info.getHeight() * 0.5F;
		int w = info.getWidth() >> 4;
		int h = info.getHeight() >> 4;
		float dx = info.getWidth() * 0.5F;
		float dy = info.getHeight() * 0.5F;
		
		int light = 0;
		BlockPos origin = painting.blockPosition();
		Direction cross = painting.getDirection().getCounterClockWise();
		POS.set(origin);
		for (int x = 0; x < w; x++) {
			POS.move(cross, x);
			for (int y = 0; y < h; y++) {
				POS.setY(origin.getY() + y);
				int l = LevelRenderer.getLightColor(painting.level(), POS);
				if (l > light) light = l;
			}
		}
		
		int rgb = WHITE;
		PaintingColorProvider provider = info.getProvider();
		if (provider != null) {
			rgb = provider.getColor((ClientLevel) painting.level(), POS);
		}
		
		this.vertex(pos, normal, vertexConsumer, rgb, -dx,  dy, -0.5f, 1, 0, light);
		this.vertex(pos, normal, vertexConsumer, rgb,  dx,  dy, -0.5f, 0, 0, light);
		this.vertex(pos, normal, vertexConsumer, rgb,  dx, -dy, -0.5f, 0, 1, light);
		this.vertex(pos, normal, vertexConsumer, rgb, -dx, -dy, -0.5f, 1, 1, light);
	}
	
	private void vertex(Matrix4f pos, Matrix3f normal, VertexConsumer vertexConsumer, int rgb, float x, float y, float z, float u, float v, int lightmap) {
		int r = (rgb >> 16) & 255;
		int g = (rgb >> 8) & 255;
		int b = rgb & 255;
		vertexConsumer
			.vertex(pos, x, y, z)
			.color(r, g, b, 255)
			.uv(u, v)
			.overlayCoords(OverlayTexture.NO_OVERLAY)
			.uv2(lightmap)
			.normal(normal, 0, 0, -1)
			.endVertex();
	}
}