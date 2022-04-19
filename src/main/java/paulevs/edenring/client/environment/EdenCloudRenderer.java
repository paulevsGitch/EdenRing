package paulevs.edenring.client.environment;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.CloudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.environment.clouds.CloudGrid;

@Environment(value= EnvType.CLIENT)
public class EdenCloudRenderer implements CloudRenderer {
	private static final ResourceLocation CLOUDS = EdenRing.makeID("textures/environment/clouds.png");
	
	private TransformHelper helper = new TransformHelper();
	private CloudGrid grid = new CloudGrid();
	
	@Override
	public void render(WorldRenderContext context) {
		PoseStack poseStack = context.matrixStack();
		ClientLevel level = context.world();
		Camera camera = context.camera();
		
		if (poseStack == null || camera == null || level == null) {
			return;
		}
		
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		
		// Init
		
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
		RenderSystem.setShaderTexture(0, CLOUDS);
		
		ShaderInstance shader = RenderSystem.getShader();
		if (shader.FOG_START != null) shader.FOG_START.set(RenderSystem.getShaderFogStart() * 2.0F);
		if (shader.FOG_END != null) shader.FOG_END.set(RenderSystem.getShaderFogEnd() * 4.0F);
		if (shader.FOG_COLOR != null) shader.FOG_COLOR.set(RenderSystem.getShaderFogColor());
		if (shader.FOG_SHAPE != null) shader.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
		
		// Start
		
		poseStack.pushPose();
		helper.applyPerspective(poseStack, camera);
		
		double time = (double) level.getGameTime() + context.tickDelta();
		ChunkPos pos = camera.getEntity().chunkPosition();
		int distance = context.gameRenderer().getMinecraft().options.renderDistance;
		grid.render(level, pos, distance << 1 | 1, bufferBuilder, poseStack, camera, time);
		
		poseStack.popPose();
		
		// Finalise
		
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}
}
