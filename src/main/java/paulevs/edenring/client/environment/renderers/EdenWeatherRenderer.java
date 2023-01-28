package paulevs.edenring.client.environment.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.WeatherRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.EdenRingClient;
import paulevs.edenring.client.environment.TransformHelper;
import paulevs.edenring.client.environment.animation.SpriteGrid;
import paulevs.edenring.client.environment.weather.LightningAnimation;
import paulevs.edenring.datagen.worldgen.EdenRingBiomesDataProvider;
import paulevs.edenring.registries.EdenBiomes;

public class EdenWeatherRenderer implements WeatherRenderer {
	private static final ResourceLocation LIGHTNING = EdenRing.makeID("textures/environment/lightning.png");
	private SpriteGrid grid = new SpriteGrid(LightningAnimation::new, (biome, random) -> biome.getBiomeKey() == EdenBiomes.BRAINSTORM ? random.nextInt(3) : 0);
	
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
		RenderSystem.setShaderTexture(0, LIGHTNING);
		context.lightmapTextureManager().turnOnLightLayer();
		
		// Start
		
		poseStack.pushPose();
		
		Minecraft minecraft = context.gameRenderer().getMinecraft();
		TransformHelper.applyPerspective(poseStack, camera);
		if (minecraft.options.bobView().get() && EdenRingClient.hasIris()) {
			TransformHelper.fixBobbing(poseStack, minecraft.player, context.tickDelta());
		}
		
		ChunkPos pos = camera.getEntity().chunkPosition();
		int distance = context.gameRenderer().getMinecraft().options.renderDistance().get();
		grid.render(level, pos, distance << 1 | 1, poseStack, camera, context.tickDelta(), null);
		
		poseStack.popPose();
		
		// Finalise
		
		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		context.lightmapTextureManager().turnOffLightLayer();
	}
}
