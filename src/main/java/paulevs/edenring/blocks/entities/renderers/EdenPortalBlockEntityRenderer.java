package paulevs.edenring.blocks.entities.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.entities.EdenPortalBlockEntity;

public class EdenPortalBlockEntityRenderer <T extends EdenPortalBlockEntity> implements BlockEntityRenderer<T> {
	private static final ResourceLocation PORTAL_RAY_DEEP = EdenRing.makeID("textures/block/portal_ray_deep.png");
	private static final ResourceLocation PORTAL_RAY = EdenRing.makeID("textures/block/portal_ray.png");
	
	public EdenPortalBlockEntityRenderer(Context context) {
		super();
	}
	
	@Override
	public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
		poseStack.pushPose();
		Matrix4f matrix = poseStack.last().pose();
		
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		
		float time1 = (float) ((((double) entity.getTicks() + tickDelta) * 0.025) % 1.0);
		float time2 = (float) ((((double) entity.getTicks() + tickDelta) * 0.04) % 1.0);
		
		float v1 = 1.0F + time1;
		float v2 = time1;
		
		float v3 = 1.0F + time2 + 0.3F;
		float v4 = time2 + 0.3F;
		
		RenderSystem.setShaderTexture(0, PORTAL_RAY_DEEP);
		renderFaces(bufferBuilder, matrix, v3, v4, 0.25F);
		
		RenderSystem.setShaderTexture(0, PORTAL_RAY);
		renderFaces(bufferBuilder, matrix, v1, v2, 0.375F);
		
		poseStack.popPose();
		
		RenderSystem.enableCull();
		RenderSystem.defaultBlendFunc();
	}
	
	private void renderFaces(BufferBuilder bufferBuilder, Matrix4f matrix, float v1, float v2, float offset) {
		float xz1 = -offset;
		float xz2 = 1.0F + offset;
		
		bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		
		bufferBuilder.vertex(matrix, xz2,  0.0F, xz1).uv(0.0F, v1).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(matrix, xz2,  3.0F, xz1).uv(0.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz2,  3.0F, xz2).uv(1.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz2,  0.0F, xz2).uv(1.0F, v1).color(255, 255, 255, 255).endVertex();
		
		bufferBuilder.vertex(matrix, xz1,  0.0F, xz2).uv(0.0F, v1).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(matrix, xz1,  3.0F, xz2).uv(0.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz1,  3.0F, xz1).uv(1.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz1,  0.0F, xz1).uv(1.0F, v1).color(255, 255, 255, 255).endVertex();
		
		bufferBuilder.vertex(matrix, xz1,  0.0F,  xz2).uv(0.0F, v1).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(matrix, xz2,  0.0F,  xz2).uv(1.0F, v1).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(matrix, xz2,  3.0F,  xz2).uv(1.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz1,  3.0F,  xz2).uv(0.0F, v2).color(255, 255, 255,   0).endVertex();
		
		bufferBuilder.vertex(matrix, xz1,  3.0F, xz1).uv(0.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz2,  3.0F, xz1).uv(1.0F, v2).color(255, 255, 255,   0).endVertex();
		bufferBuilder.vertex(matrix, xz2,  0.0F, xz1).uv(1.0F, v1).color(255, 255, 255, 255).endVertex();
		bufferBuilder.vertex(matrix, xz1,  0.0F, xz1).uv(0.0F, v1).color(255, 255, 255, 255).endVertex();
		
		bufferBuilder.end();
		BufferUploader.end(bufferBuilder);
	}
}