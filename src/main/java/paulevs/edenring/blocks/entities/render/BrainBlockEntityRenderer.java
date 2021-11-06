package paulevs.edenring.blocks.entities.render;

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
import net.minecraft.util.Mth;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.entities.BrainTreeBlockEntity;

public class BrainBlockEntityRenderer <T extends BrainTreeBlockEntity> implements BlockEntityRenderer<T> {
	private static final ResourceLocation LIGHNING = EdenRing.makeID("textures/block/lightning.png");
	private static final float VERTICAL_SIDE = 1F / 5F;
	
	public BrainBlockEntityRenderer(Context context) {
		super();
	}
	
	@Override
	public void render(T entity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
		int status = entity.getStatus();
		
		if (status == 0 || status != 0) {
			return;
		}
		
		if (status == 0 && !entity.isActive()) {
			return;
		}
		
		float sinAngle = (float) ((((double) entity.getAnimation() + tickDelta) * 0.1) % (Math.PI * 2));
		float animation = (status + tickDelta) / 5.0F;
		if (animation > 1) {
			animation = 1;
		}
		
		float alpha = 0.75F;
		float scale = 1.1F + Mth.sin(sinAngle) * 0.05F;
		if (entity.isActive()) {
			alpha *= animation;
			scale = Mth.lerp(animation, 1.0F, scale);
		}
		else {
			alpha *= 1.0F - animation;
			scale = Mth.lerp(animation, scale, 1.0F);
		}
		
		float offset = (scale - 1.0F) * 0.5F;
		poseStack.pushPose();
		poseStack.scale(scale, scale, scale);
		poseStack.translate(-offset, -offset, -offset);
		Matrix4f matrix = poseStack.last().pose();
		
		//RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, LIGHNING);
		RenderSystem.setShaderColor(1, 1, 1, alpha);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		float time = Mth.floor((entity.getAnimation() & 15) / 16F * 5.0F);
		float v1 = time / 5F;
		float v2 = (time + 1) / 5F;
		
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 1.0F).uv(0.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 1.0F).uv(1.0F, v2).endVertex();
		
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 1.0F).uv(0.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 1.0F).uv(1.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 0.0F).uv(0.0F, v1).endVertex();
		
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 0.0F).uv(0.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 1.0F).uv(1.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 1.0F).uv(0.0F, v2).endVertex();
		
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 1.0F).uv(0.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 1.0F).uv(1.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, v1).endVertex();
		
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 1.0F).uv(0.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 1.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 1.0F).uv(1.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 1.0F).uv(0.0F, v2).endVertex();
		
		bufferBuilder.vertex(matrix, 0.0F, 1.0F, 0.0F).uv(0.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 1.0F, 0.0F).uv(1.0F, v2).endVertex();
		bufferBuilder.vertex(matrix, 1.0F, 0.0F, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, v1).endVertex();
		
		bufferBuilder.end();
		
		BufferUploader.end(bufferBuilder);
		
		poseStack.popPose();
		
		RenderSystem.defaultBlendFunc();
	}
}
