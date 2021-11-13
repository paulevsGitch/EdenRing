package paulevs.edenring.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.world.MoonInfo;
import ru.bclib.util.BackgroundInfo;
import ru.bclib.util.MHelper;

import java.util.Random;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Final
	@Shadow
	private Minecraft minecraft;
	
	@Shadow
	private int ticks;
	
	private static final ResourceLocation EDEN_PLANET_TEXTURE = EdenRing.makeID("textures/environment/planet.png");
	private static final ResourceLocation EDEN_MOON_TEXTURE = EdenRing.makeID("textures/environment/moon.png");
	private static final ResourceLocation EDEN_RINGS_SOFT_TEXTURE = EdenRing.makeID("textures/environment/rings_soft.png");
	private static final ResourceLocation EDEN_RINGS_TEXTURE = EdenRing.makeID("textures/environment/rings.png");
	private static final ResourceLocation EDEN_HORIZON_BW = EdenRing.makeID("textures/environment/horizon_bw.png");
	private static final ResourceLocation EDEN_HORIZON = EdenRing.makeID("textures/environment/horizon.png");
	private static final ResourceLocation EDEN_NEBULA1 = EdenRing.makeID("textures/environment/nebula_1.png");
	private static final ResourceLocation EDEN_NEBULA2 = EdenRing.makeID("textures/environment/nebula_2.png");
	private static final ResourceLocation EDEN_STARS = EdenRing.makeID("textures/environment/stars.png");
	private static final ResourceLocation EDEN_SUN_FADE = EdenRing.makeID("textures/environment/sun_fade.png");
	private static final ResourceLocation EDEN_SUN = EdenRing.makeID("textures/environment/sun.png");
	private static final ResourceLocation EDEN_FRAME = EdenRing.makeID("textures/environment/frame.png");
	private static final MoonInfo[] EDEN_MOONS = new MoonInfo[8];
	
	private static BufferBuilder eden_bufferBuilder;
	private static VertexBuffer[] eden_horizon;
	private static VertexBuffer eden_stars;
	private static VertexBuffer[] eden_nebula;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void eden_onRendererInit(Minecraft client, RenderBuffers bufferBuilders, CallbackInfo info) {
		eden_bufferBuilder = Tesselator.getInstance().getBuilder();
		
		if (eden_horizon == null) {
			eden_horizon = new VertexBuffer[2];
		}
		
		if (eden_nebula == null) {
			eden_nebula = new VertexBuffer[3];
		}
		
		eden_horizon[0] = eden_buildBufferHorizon(eden_bufferBuilder, eden_horizon[0], 20);
		eden_horizon[1] = eden_buildBufferHorizon(eden_bufferBuilder, eden_horizon[1], 40);
		eden_nebula[0] = eden_buildBufferHorizon(eden_bufferBuilder, eden_nebula[0], 30);
		
		eden_stars = eden_buildBufferStars(eden_bufferBuilder, eden_stars, 0.1, 0.7, 5000, 4, 41315);
		
		eden_nebula[1] = eden_buildBufferStars(eden_bufferBuilder, eden_nebula[1], 20, 60, 10, 1, 235);
		eden_nebula[2] = eden_buildBufferStars(eden_bufferBuilder, eden_nebula[2], 20, 60, 10, 1, 352);
		
		Random random = new Random(0);
		for (int i = 0; i < EDEN_MOONS.length; i++) {
			EDEN_MOONS[i] = new MoonInfo(random);
		}
	}
	
	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	private void eden_renderSky(PoseStack matrices, Matrix4f matrix4f, float tickDelta, Runnable runnable, CallbackInfo info) {
		if (eden_isInIden()) {
			runnable.run();
			
			double time = (double) ticks + tickDelta;
			double py = minecraft.player.position().y;
			float angle = Mth.clamp((float) (py - 128) * 0.0006F, -0.03F, 0.03F);
			float skyBlend = Mth.clamp((float) Math.abs(py - 128) * 0.006F, 0.0F, 1.0F);
			if (py < 0) {
				py = Mth.clamp(-py * 0.05, 0, 1);
			}
			else if (py < 256) {
				py = 0;
			}
			else {
				py = Mth.clamp((py - 256) * 0.05, 0, 1);
			}
			
			// Get Sky Color //
			
			Vec3 skyColor = minecraft.level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), tickDelta);
			float skyR = Mth.lerp(skyBlend, (float) skyColor.x * 0.5F, 0);
			float skyG = Mth.lerp(skyBlend, (float) skyColor.y * 0.5F, 0);
			float skyB = Mth.lerp(skyBlend, (float) skyColor.z * 0.5F, 0);
			
			// Init Setup //
			
			RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);
			FogRenderer.setupNoFog();
			RenderSystem.depthMask(false);
			RenderSystem.disableBlend();
			RenderSystem.disableCull();
			RenderSystem.disableTexture();
			RenderSystem.disableDepthTest();
			
			// Render Background //
			
			RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
			RenderSystem.setShader(GameRenderer::getPositionShader);
			eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			eden_bufferBuilder.vertex(matrix4f, -10.0F, -10.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix4f,  10.0F, -10.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix4f,  10.0F,  10.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix4f, -10.0F,  10.0F, 0.0F).endVertex();
			eden_bufferBuilder.end();
			BufferUploader.end(eden_bufferBuilder);
			
			// Render Nebula And Stars //
			
			float dayTime = minecraft.level.getTimeOfDay(tickDelta);
			
			RenderSystem.enableTexture();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.enableBlend();
			
			matrices.pushPose();
			matrices.mulPose(Vector3f.XP.rotation(-0.4F));
			matrices.mulPose(Vector3f.YP.rotation((float) Math.PI * 0.5F - dayTime * (float) Math.PI * 2.0F));
			
			RenderSystem.setShaderTexture(0, EDEN_STARS);
			eden_renderBuffer(matrices, matrix4f, eden_stars, DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, skyBlend * 0.5F + 0.5F);
			
			float nebulaBlend = skyBlend * 0.75F + 0.25F;
			float nebulaBlend2 = nebulaBlend * 0.15F;
			RenderSystem.setShaderTexture(0, EDEN_NEBULA1);
			eden_renderBuffer(matrices, matrix4f, eden_nebula[1], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend2);
			
			RenderSystem.setShaderTexture(0, EDEN_NEBULA2);
			eden_renderBuffer(matrices, matrix4f, eden_nebula[2], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend2);
			
			RenderSystem.setShaderTexture(0, EDEN_HORIZON);
			eden_renderBuffer(matrices, matrix4f, eden_nebula[0], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend);
			
			// Render Sun //
			
			matrices.pushPose();
			matrices.mulPose(Vector3f.ZP.rotation((float) Math.PI * 0.5F));
			Matrix4f matrix = matrices.last().pose();
			
			RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
			RenderSystem.setShaderTexture(0, EDEN_SUN_FADE);
			eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			eden_bufferBuilder.vertex(matrix, -80.0F, 100.0F, -80.0F).uv(0.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  80.0F, 100.0F, -80.0F).uv(1.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  80.0F, 100.0F,  80.0F).uv(1.0F, 1.0F).endVertex();
			eden_bufferBuilder.vertex(matrix, -80.0F, 100.0F,  80.0F).uv(0.0F, 1.0F).endVertex();
			eden_bufferBuilder.end();
			BufferUploader.end(eden_bufferBuilder);
			
			float color = (float) Math.cos(dayTime * Math.PI * 2) * 1.1F;
			color = Mth.clamp(color, 0.3F, 1.0F);
			RenderSystem.setShaderColor(1.0F, color, color, 1.0F);
			RenderSystem.setShaderTexture(0, EDEN_SUN);
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			eden_bufferBuilder.vertex(matrix, -30.0F, 100.0F, -30.0F).uv(0.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  30.0F, 100.0F, -30.0F).uv(1.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  30.0F, 100.0F,  30.0F).uv(1.0F, 1.0F).endVertex();
			eden_bufferBuilder.vertex(matrix, -30.0F, 100.0F,  30.0F).uv(0.0F, 1.0F).endVertex();
			eden_bufferBuilder.end();
			BufferUploader.end(eden_bufferBuilder);
			RenderSystem.defaultBlendFunc();
			
			matrices.popPose();
			
			matrices.popPose();
			
			// Setup Perspective //
			
			RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
			RenderSystem.depthMask(true);
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			
			// Render Rings //
			
			matrices.pushPose();
			matrices.translate(0, 0, -100);
			matrices.mulPose(Vector3f.XP.rotation(angle));
			
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, EDEN_RINGS_SOFT_TEXTURE);
			
			matrix = matrices.last().pose();
			eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			eden_bufferBuilder.vertex(matrix, -130.0F, 0.0F, -130.0F).uv(0.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  130.0F, 0.0F, -130.0F).uv(1.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  130.0F, 0.0F,  130.0F).uv(1.0F, 1.0F).endVertex();
			eden_bufferBuilder.vertex(matrix, -130.0F, 0.0F,  130.0F).uv(0.0F, 1.0F).endVertex();
			eden_bufferBuilder.end();
			BufferUploader.end(eden_bufferBuilder);
			
			if (py > 0) {
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) py);
				RenderSystem.setShaderTexture(0, EDEN_RINGS_TEXTURE);
				eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				eden_bufferBuilder.vertex(matrix, -130.0F, 0.01F, -130.0F).uv(0.0F, 0.0F).endVertex();
				eden_bufferBuilder.vertex(matrix,  130.0F, 0.01F, -130.0F).uv(1.0F, 0.0F).endVertex();
				eden_bufferBuilder.vertex(matrix,  130.0F, 0.01F,  130.0F).uv(1.0F, 1.0F).endVertex();
				eden_bufferBuilder.vertex(matrix, -130.0F, 0.01F,  130.0F).uv(0.0F, 1.0F).endVertex();
				eden_bufferBuilder.end();
				BufferUploader.end(eden_bufferBuilder);
			}
			
			matrices.popPose();
			
			// Render Moons //
			
			int frame = (int) (dayTime * 12);
			float v0 = frame / 12F;
			float v1 = v0 + 0.083F;
			
			for (int i = 0; i < EDEN_MOONS.length; i++) {
				MoonInfo moon = EDEN_MOONS[i];
				double position = (moon.orbitState + dayTime) * moon.speed;
				eden_renderMoon(matrices, position, moon.orbitRadius, moon.orbitAngle, moon.size, v0, v1, moon.color);
			}
			
			// Render Planet //
			
			matrices.pushPose();
			matrices.translate(0, 0, -100);
			
			matrix = matrices.last().pose();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, EDEN_PLANET_TEXTURE);
			eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			eden_bufferBuilder.vertex(matrix, -140,  140, 0.0F).uv(0.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  140,  140, 0.0F).uv(1.0F, 0.0F).endVertex();
			eden_bufferBuilder.vertex(matrix,  140, -140, 0.0F).uv(1.0F, 1.0F).endVertex();
			eden_bufferBuilder.vertex(matrix, -140, -140, 0.0F).uv(0.0F, 1.0F).endVertex();
			eden_bufferBuilder.end();
			BufferUploader.end(eden_bufferBuilder);
			
			matrices.popPose();
			
			// Render Fog //
			
			if (py < 1) {
				RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
				RenderSystem.depthMask(false);
				RenderSystem.disableDepthTest();
				RenderSystem.setShaderTexture(0, EDEN_HORIZON_BW);
				
				matrices.pushPose();
				matrices.mulPose(Vector3f.YP.rotation((float) (time * 0.0002)));
				float density = Mth.clamp(BackgroundInfo.fogDensity, 1, 2);
				eden_renderBuffer(
					matrices,
					matrix4f,
					eden_horizon[0],
					DefaultVertexFormat.POSITION_TEX,
					BackgroundInfo.fogColorRed,
					BackgroundInfo.fogColorGreen,
					BackgroundInfo.fogColorBlue,
					(1.0F - skyBlend) * 0.5F * density
				);
				matrices.popPose();
				
				matrices.pushPose();
				matrices.mulPose(Vector3f.YP.rotation((float) (-time * 0.0001)));
				eden_renderBuffer(
					matrices,
					matrix4f,
					eden_horizon[1],
					DefaultVertexFormat.POSITION_TEX,
					BackgroundInfo.fogColorRed,
					BackgroundInfo.fogColorGreen,
					BackgroundInfo.fogColorBlue,
					(1.0F - skyBlend) * 0.5F
				);
				matrices.popPose();
			}
			
			// Render Blindness //
			
			if (BackgroundInfo.blindness > 0) {
				RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
				RenderSystem.disableDepthTest();
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableTexture();
				
				RenderSystem.setShaderColor(0, 0, 0, BackgroundInfo.blindness);
				RenderSystem.setShader(GameRenderer::getPositionShader);
				eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
				eden_bufferBuilder.vertex(matrix4f, -10.0F, -10.0F, 0.0F).endVertex();
				eden_bufferBuilder.vertex(matrix4f, 10.0F, -10.0F, 0.0F).endVertex();
				eden_bufferBuilder.vertex(matrix4f, 10.0F, 10.0F, 0.0F).endVertex();
				eden_bufferBuilder.vertex(matrix4f, -10.0F, 10.0F, 0.0F).endVertex();
				eden_bufferBuilder.end();
				BufferUploader.end(eden_bufferBuilder);
			}
			
			// Finalize //
			
			RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
			RenderSystem.enableTexture();
			RenderSystem.depthMask(true);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableDepthTest();
			RenderSystem.enableCull();
			
			info.cancel();
		}
	}
	
	@Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
	public void eden_cancelCloudsRender(PoseStack poseStack, Matrix4f matrix4f, float f, double d, double e, double g, CallbackInfo info) {
		if (eden_isInIden()) {
			info.cancel();
		}
	}
	
	@Inject(method = "renderLevel", at = @At("TAIL"))
	public void eden_renderLevel(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo info) {
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
	
	private boolean eden_isInIden() {
		return minecraft.level.dimension() == EdenRing.EDEN_RING_KEY;
	}
	
	private VertexBuffer eden_buildBufferHorizon(BufferBuilder bufferBuilder, VertexBuffer buffer, double height) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		eden_makeCylinder(bufferBuilder, 16, height, 100);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private void eden_renderBuffer(PoseStack matrices, Matrix4f matrix4f, VertexBuffer buffer, VertexFormat format, float r, float g, float b, float a) {
		RenderSystem.setShaderColor(r, g, b, a);
		if (format == DefaultVertexFormat.POSITION) {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionShader());
		}
		else {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionTexShader());
		}
	}
	
	private void eden_makeCylinder(BufferBuilder buffer, int segments, double height, double radius) {
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		for (int i = 0; i < segments; i++) {
			double a1 = (double) i * Math.PI * 2.0 / (double) segments;
			double a2 = (double) (i + 1) * Math.PI * 2.0 / (double) segments;
			double px1 = Math.sin(a1) * radius;
			double pz1 = Math.cos(a1) * radius;
			double px2 = Math.sin(a2) * radius;
			double pz2 = Math.cos(a2) * radius;
			
			float u0 = (float) i / (float) segments;
			float u1 = (float) (i + 1) / (float) segments;
			
			buffer.vertex(px1, -height, pz1).uv(u0, 0).endVertex();
			buffer.vertex(px1, height, pz1).uv(u0, 1).endVertex();
			buffer.vertex(px2, height, pz2).uv(u1, 1).endVertex();
			buffer.vertex(px2, -height, pz2).uv(u1, 0).endVertex();
		}
	}
	
	private VertexBuffer eden_buildBufferStars(BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, int verticalCount, long seed) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer();
		eden_makeStars(bufferBuilder, minSize, maxSize, count, verticalCount, seed);
		bufferBuilder.end();
		buffer.upload(bufferBuilder);
		
		return buffer;
	}
	
	private void eden_makeStars(BufferBuilder buffer, double minSize, double maxSize, int count, int verticalCount, long seed) {
		Random random = new Random(seed);
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		
		for (int i = 0; i < count; ++i) {
			double posX = random.nextDouble() * 2.0 - 1.0;
			double posY = random.nextDouble() * 2.0 - 1.0;
			double posZ = random.nextDouble() * 2.0 - 1.0;
			double size = MHelper.randRange(minSize, maxSize, random);
			double length = posX * posX + posY * posY + posZ * posZ;
			if (length < 1.0 && length > 0.001) {
				length = 1.0 / Math.sqrt(length);
				posX *= length;
				posY *= length;
				posZ *= length;
				double j = posX * 100.0;
				double k = posY * 100.0;
				double l = posZ * 100.0;
				double m = Math.atan2(posX, posZ);
				double n = Math.sin(m);
				double o = Math.cos(m);
				double p = Math.atan2(Math.sqrt(posX * posX + posZ * posZ), posY);
				double q = Math.sin(p);
				double r = Math.cos(p);
				double s = random.nextDouble() * Math.PI * 2.0;
				double t = Math.sin(s);
				double u = Math.cos(s);
				
				int pos = 0;
				float minV = verticalCount < 2 ? 0 : (float) random.nextInt(verticalCount) / verticalCount;
				for (int v = 0; v < 4; ++v) {
					double x = (double) ((v & 2) - 1) * size;
					double y = (double) ((v + 1 & 2) - 1) * size;
					double aa = x * u - y * t;
					double ab = y * u + x * t;
					double ad = aa * q + 0.0 * r;
					double ae = 0.0 * q - aa * r;
					double af = ae * n - ab * o;
					double ah = ab * n + ae * o;
					float texU = (pos >> 1) & 1;
					float texV = (float) (((pos + 1) >> 1) & 1) / verticalCount + minV;
					pos++;
					buffer.vertex(j + af, k + ad, l + ah).uv(texU, texV).endVertex();
				}
			}
		}
	}
	
	private void eden_renderMoon(PoseStack matrices, double orbitPosition, float orbitRadius, float orbitAngle, float size, float v0, float v1, Vector3f color) {
		float offset1 = (float) Math.sin(orbitPosition);
		float offset2 = (float) Math.cos(orbitPosition);
		
		matrices.pushPose();
		matrices.translate(offset1 * (70 + orbitRadius), offset1 * orbitAngle, offset2 * orbitRadius - 100);
		
		Matrix4f matrix = matrices.last().pose();
		RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1F);
		RenderSystem.setShaderTexture(0, EDEN_MOON_TEXTURE);
		eden_bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		eden_bufferBuilder.vertex(matrix, -size,  size, 0.0F).uv(0.0F, v0).endVertex();
		eden_bufferBuilder.vertex(matrix,  size,  size, 0.0F).uv(1.0F, v0).endVertex();
		eden_bufferBuilder.vertex(matrix,  size, -size, 0.0F).uv(1.0F, v1).endVertex();
		eden_bufferBuilder.vertex(matrix, -size, -size, 0.0F).uv(0.0F, v1).endVertex();
		eden_bufferBuilder.end();
		BufferUploader.end(eden_bufferBuilder);
		
		matrices.popPose();
	}
}
