package paulevs.edenring.client.environment.renderers;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.CloudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.environment.TransformHelper;
import paulevs.edenring.client.environment.animation.SpriteGrid;
import paulevs.edenring.client.environment.weather.CloudAnimation;

@Environment(value= EnvType.CLIENT)
public class EdenCloudRenderer implements CloudRenderer {
	private static final ResourceLocation CLOUDS = EdenRing.makeID("textures/environment/clouds.png");
	private SpriteGrid grid = new SpriteGrid(CloudAnimation::new, (biome, random) -> {
		float fog = biome.getFogDensity();
		if (fog > 1 && random.nextInt(5) > 0) {
			return (int) (random.nextFloat() * fog * 2);
		}
		return random.nextInt(16) == 0 ? 1 : 0;
	});
	
	@Override
	public void render(WorldRenderContext context) {
		PoseStack poseStack = context.matrixStack();
		ClientLevel level = context.world();
		Camera camera = context.camera();
		
		if (poseStack == null || camera == null || level == null) return;
		
		// Init
		
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, CLOUDS);
		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		
		// Start
		
		poseStack.pushPose();
		TransformHelper.applyPerspective(poseStack, camera);
		
		ChunkPos pos = camera.getEntity().chunkPosition();
		int distance = context.gameRenderer().getMinecraft().options.renderDistance;
		grid.render(level, pos, distance << 1 | 1, poseStack, camera, context.tickDelta(), context.frustum());
		
		poseStack.popPose();
		
		// Finalise
		
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
	}
}
