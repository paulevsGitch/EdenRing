package paulevs.edenring.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Final
	@Shadow
	private Minecraft minecraft;
	
	private static final ResourceLocation EDEN_FRAME = EdenRing.makeID("textures/environment/frame.png");
	private static BufferBuilder eden_bufferBuilder;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void eden_onRendererInit(Minecraft client, RenderBuffers bufferBuilders, CallbackInfo info) {
		eden_bufferBuilder = Tesselator.getInstance().getBuilder();
	}
	
	@Inject(method = "renderLevel", at = @At("TAIL"))
	public void eden_renderLevel(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo info) {
		renderMushroomFrame(poseStack, camera);
	}
	
	private void renderMushroomFrame(PoseStack poseStack, Camera camera) {
		if (minecraft.hitResult == null || !(minecraft.hitResult instanceof BlockHitResult)) {
			return;
		}
		
		ItemStack item = minecraft.player.getMainHandItem();
		if (item != null && !(item.getItem() instanceof BlockItem) || ((BlockItem) item.getItem()).getBlock() != EdenBlocks.BALLOON_MUSHROOM_BLOCK) {
			return;
		}
		
		poseStack.pushPose();
		
		BlockHitResult bnr = (BlockHitResult) minecraft.hitResult;
		if (!minecraft.level.getBlockState(bnr.getBlockPos()).isAir()) {
			return;
		}
		
		double dx = bnr.getBlockPos().getX();
		double dy = bnr.getBlockPos().getY();
		double dz = bnr.getBlockPos().getZ();
		
		dx -= camera.getPosition().x;
		dy -= camera.getPosition().y;
		dz -= camera.getPosition().z;
		poseStack.translate(dx, dy, dz);
		
		Matrix4f matrix = poseStack.last().pose();
		
		RenderSystem.enableTexture();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, EDEN_FRAME);
		
		eden_bufferBuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).uv(0.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F, -0.001F, -0.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F, -0.001F,  1.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F,  1.001F).uv(0.0F, 1.0F).endVertex();
		
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F,  1.001F).uv(0.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F,  1.001F,  1.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F,  1.001F, -0.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F, -0.001F).uv(0.0F, 0.0F).endVertex();
		
		eden_bufferBuilder.vertex(matrix, 1.001F, -0.001F, -0.001F).uv(0.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, 1.001F,  1.001F, -0.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, 1.001F,  1.001F,  1.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, 1.001F, -0.001F,  1.001F).uv(0.0F, 1.0F).endVertex();
		
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F,  1.001F).uv(0.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F,  1.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F, -0.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).uv(0.0F, 0.0F).endVertex();
		
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F,  1.001F).uv(0.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F, -0.001F,  1.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F,  1.001F,  1.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F,  1.001F).uv(0.0F, 1.0F).endVertex();
		
		eden_bufferBuilder.vertex(matrix, -0.001F,  1.001F, -0.001F).uv(0.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F,  1.001F, -0.001F).uv(1.0F, 1.0F).endVertex();
		eden_bufferBuilder.vertex(matrix,  1.001F, -0.001F, -0.001F).uv(1.0F, 0.0F).endVertex();
		eden_bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).uv(0.0F, 0.0F).endVertex();
		
		eden_bufferBuilder.end();
		
		BufferUploader.end(eden_bufferBuilder);
		
		poseStack.popPose();
	}
}
