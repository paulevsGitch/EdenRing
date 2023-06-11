package paulevs.edenring.client.environment.renderers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.SkyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.phys.Vec3;
import org.betterx.bclib.util.BackgroundInfo;
import org.betterx.bclib.util.MHelper;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import paulevs.edenring.EdenRing;
import paulevs.edenring.config.Configs;
import paulevs.edenring.world.MoonInfo;

@Environment(value= EnvType.CLIENT)
public class EdenSkyRenderer implements SkyRenderer {
	private static final ResourceLocation PLANET_TEXTURE = EdenRing.makeID("textures/environment/planet.png");
	private static final ResourceLocation MOON_TEXTURE = EdenRing.makeID("textures/environment/moon.png");
	private static final ResourceLocation RINGS_SOFT_TEXTURE = EdenRing.makeID("textures/environment/rings_soft.png");
	private static final ResourceLocation RINGS_TEXTURE = EdenRing.makeID("textures/environment/rings.png");
	private static final ResourceLocation HORIZON_BW = EdenRing.makeID("textures/environment/horizon_bw.png");
	private static final ResourceLocation HORIZON = EdenRing.makeID("textures/environment/horizon.png");
	private static final ResourceLocation NEBULA1 = EdenRing.makeID("textures/environment/nebula_1.png");
	private static final ResourceLocation NEBULA2 = EdenRing.makeID("textures/environment/nebula_2.png");
	private static final ResourceLocation STARS = EdenRing.makeID("textures/environment/stars.png");
	private static final ResourceLocation SUN_FADE = EdenRing.makeID("textures/environment/sun_fade.png");
	private static final ResourceLocation SUN = EdenRing.makeID("textures/environment/sun.png");
	private static final MoonInfo[] MOONS = new MoonInfo[8];
	
	private static BufferBuilder bufferBuilder;
	private static VertexBuffer[] horizon;
	private static VertexBuffer[] nebula;
	private static VertexBuffer stars;
	
	private boolean shouldInit = true;
	
	private void init() {
		shouldInit = false;
		bufferBuilder = Tesselator.getInstance().getBuilder();
		
		if (horizon == null) {
			horizon = new VertexBuffer[3];
		}
		
		if (nebula == null) {
			nebula = new VertexBuffer[3];
		}
		
		horizon[0] = buildBufferCylinder(bufferBuilder, horizon[0], 20);
		horizon[1] = buildBufferCylinder(bufferBuilder, horizon[1], 40);
		horizon[2] = buildBufferCylinder(bufferBuilder, horizon[2], 100);
		
		nebula[0] = buildBufferCylinder(bufferBuilder, nebula[0], 30);
		nebula[1] = buildBufferSquares(bufferBuilder, nebula[1], 20, 60, 10, 1, 235);
		nebula[2] = buildBufferSquares(bufferBuilder, nebula[2], 20, 60, 10, 1, 352);
		
		stars = buildBufferSquares(bufferBuilder, stars, 0.125, 0.875, 5000, 4, 41315);
		
		RandomSource random = new XoroshiroRandomSource(0);
		for (int i = 0; i < MOONS.length; i++) {
			MOONS[i] = new MoonInfo(random);
		}
	}
	
	@Override
	public void render(WorldRenderContext context) {
		if (!Configs.CLIENT_CONFIG.renderSky()) {
			return;
		}
		
		ClientLevel level = context.world();
		Matrix4f projectionMatrix = context.projectionMatrix();
		
		if (level == null || projectionMatrix == null) {
			return;
		}
		
		Minecraft minecraft = Minecraft.getInstance();
		PoseStack poseStack = context.matrixStack();
		float tickDelta = context.tickDelta();
		
		if (shouldInit) {
			init();
		}
		
		double time = (double) level.getGameTime() + tickDelta;
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
		
		Vec3 skyColor = getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), tickDelta, level);
		float skyR = Mth.lerp(skyBlend, (float) skyColor.x * 0.5F, 0);
		float skyG = Mth.lerp(skyBlend, (float) skyColor.y * 0.5F, 0);
		float skyB = Mth.lerp(skyBlend, (float) skyColor.z * 0.5F, 0);
		
		// Init Setup //
		
		RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);
		FogRenderer.setupNoFog();
		RenderSystem.depthMask(false);
		RenderSystem.disableBlend();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		
		// Render Background //
		
		RenderSystem.clearColor(skyR, skyG, skyB, 1.0F);
		RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
		bufferBuilder.vertex(projectionMatrix, -10.0F, -10.0F, 0.0F).endVertex();
		bufferBuilder.vertex(projectionMatrix,  10.0F, -10.0F, 0.0F).endVertex();
		bufferBuilder.vertex(projectionMatrix,  10.0F,  10.0F, 0.0F).endVertex();
		bufferBuilder.vertex(projectionMatrix, -10.0F,  10.0F, 0.0F).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		
		// Render Nebula And Stars //
		
		float dayTime = level.getTimeOfDay(tickDelta);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.enableBlend();
		
		poseStack.pushPose();
		poseStack.mulPose(Axis.XP.rotation(-0.4F));
		poseStack.mulPose(Axis.YP.rotation((float) Math.PI * 0.5F - dayTime * (float) Math.PI * 2.0F));
		
		RenderSystem.setShaderTexture(0, STARS);
		renderBuffer(poseStack, projectionMatrix, stars, DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, skyBlend * 0.25F + 0.75F);
		
		float nebulaBlend = skyBlend * 0.75F + 0.25F;
		float nebulaBlend2 = nebulaBlend * 0.15F;
		RenderSystem.setShaderTexture(0, NEBULA1);
		renderBuffer(poseStack, projectionMatrix, nebula[1], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend2);
		
		RenderSystem.setShaderTexture(0, NEBULA2);
		renderBuffer(poseStack, projectionMatrix, nebula[2], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend2);
		
		RenderSystem.setShaderTexture(0, HORIZON);
		renderBuffer(poseStack, projectionMatrix, nebula[0], DefaultVertexFormat.POSITION_TEX, 1.0F, 1.0F, 1.0F, nebulaBlend);
		
		// Render Sun //
		
		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotation((float) Math.PI * 0.5F));
		Matrix4f matrix = poseStack.last().pose();
		
		RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
		RenderSystem.setShaderTexture(0, SUN_FADE);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, -80.0F, 100.0F, -80.0F).uv(0.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  80.0F, 100.0F, -80.0F).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  80.0F, 100.0F,  80.0F).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(matrix, -80.0F, 100.0F,  80.0F).uv(0.0F, 1.0F).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		
		float color = (float) Math.cos(dayTime * Math.PI * 2) * 1.1F;
		color = Mth.clamp(color, 0.3F, 1.0F);
		RenderSystem.setShaderColor(1.0F, color, color, 1.0F);
		RenderSystem.setShaderTexture(0, SUN);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, -30.0F, 100.0F, -30.0F).uv(0.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  30.0F, 100.0F, -30.0F).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  30.0F, 100.0F,  30.0F).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(matrix, -30.0F, 100.0F,  30.0F).uv(0.0F, 1.0F).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		RenderSystem.defaultBlendFunc();
		
		poseStack.popPose();
		
		poseStack.popPose();
		
		// Setup Perspective //
		
		RenderSystem.defaultBlendFunc();
		
		// Render Rings Back //
		
		poseStack.pushPose();
		poseStack.translate(0, 0, -100);
		poseStack.mulPose(Axis.XP.rotation(angle));
		
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, RINGS_SOFT_TEXTURE);
		
		matrix = poseStack.last().pose();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, -130.0F, 0.0F, -130.0F).uv(0.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  130.0F, 0.0F, -130.0F).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  130.0F, 0.0F,  130.0F).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(matrix, -130.0F, 0.0F,  130.0F).uv(0.0F, 1.0F).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		
		poseStack.popPose();
		
		// Render Moons Back //
		
		int frame = (int) (dayTime * 12 + 0.5F);
		float v0 = frame / 12F;
		float v1 = v0 + 0.083F;
		
		for (int i = 0; i < MOONS.length; i++) {
			MoonInfo moon = MOONS[i];
			double position = moon.orbitState + dayTime * moon.speed;
			renderMoon(false, poseStack, position, moon.orbitRadius, moon.orbitAngle, moon.size, v0, v1, moon.color);
		}
		
		// Render Planet //
		
		poseStack.pushPose();
		poseStack.translate(0, 0, -100);
		
		matrix = poseStack.last().pose();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, PLANET_TEXTURE);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, -140,  140, 0.0F).uv(0.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  140,  140, 0.0F).uv(1.0F, 0.0F).endVertex();
		bufferBuilder.vertex(matrix,  140, -140, 0.0F).uv(1.0F, 1.0F).endVertex();
		bufferBuilder.vertex(matrix, -140, -140, 0.0F).uv(0.0F, 1.0F).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		
		poseStack.popPose();
		
		// Render Moons Front //

		for (MoonInfo moon : MOONS) {
			double position = moon.orbitState + dayTime * moon.speed;
			renderMoon(true, poseStack, position, moon.orbitRadius, moon.orbitAngle, moon.size, v0, v1, moon.color);
		}
		
		// Render Rings Front //
		
		if (py > 0) {
			poseStack.pushPose();
			poseStack.translate(0, 0, -100);
			poseStack.mulPose(Axis.XP.rotation(angle));
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, py > 0.5F ? 0.5F : (float) py);
			RenderSystem.setShaderTexture(0, RINGS_TEXTURE);
			matrix = poseStack.last().pose();
			bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			bufferBuilder.vertex(matrix, -130.0F, 0.001F,   0.0F).uv(0.0F, 0.5F).endVertex();
			bufferBuilder.vertex(matrix,  130.0F, 0.001F,   0.0F).uv(1.0F, 0.5F).endVertex();
			bufferBuilder.vertex(matrix,  130.0F, 0.001F, 130.0F).uv(1.0F, 1.0F).endVertex();
			bufferBuilder.vertex(matrix, -130.0F, 0.001F, 130.0F).uv(0.0F, 1.0F).endVertex();
			BufferUploader.drawWithShader(bufferBuilder.end());
			poseStack.popPose();
		}
		
		// Render Fog //
		
		if (py < 1) {
			RenderSystem.setShaderTexture(0, HORIZON_BW);
			
			if (BackgroundInfo.fogDensity > 1.5F) {
				float density = Mth.clamp(BackgroundInfo.fogDensity - 1.5F, 0, 1);
				poseStack.pushPose();
				poseStack.mulPose(Axis.YP.rotation((float) (-time * 0.00003)));
				renderBuffer(
					poseStack,
					projectionMatrix,
					horizon[1],
					DefaultVertexFormat.POSITION_TEX,
					BackgroundInfo.fogColorRed * 0.7F,
					BackgroundInfo.fogColorGreen * 0.7F,
					BackgroundInfo.fogColorBlue * 0.7F,
					(1.0F - skyBlend) * density
				);
				poseStack.popPose();
			}
			
			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotation((float) (time * 0.0002)));
			float density = Mth.clamp(BackgroundInfo.fogDensity, 1, 2);
			renderBuffer(
				poseStack,
				projectionMatrix,
				horizon[0],
				DefaultVertexFormat.POSITION_TEX,
				BackgroundInfo.fogColorRed,
				BackgroundInfo.fogColorGreen,
				BackgroundInfo.fogColorBlue,
				(1.0F - skyBlend) * 0.5F * density
			);
			poseStack.popPose();
			
			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotation((float) (-time * 0.0001)));
			renderBuffer(
				poseStack,
				projectionMatrix,
				horizon[1],
				DefaultVertexFormat.POSITION_TEX,
				BackgroundInfo.fogColorRed,
				BackgroundInfo.fogColorGreen,
				BackgroundInfo.fogColorBlue,
				(1.0F - skyBlend) * 0.5F
			);
			poseStack.popPose();
		}
		
		// Render Blindness //
		
		if (BackgroundInfo.blindness > 0) {
			RenderSystem.defaultBlendFunc();
			
			RenderSystem.setShaderColor(0, 0, 0, BackgroundInfo.blindness);
			RenderSystem.setShader(GameRenderer::getPositionShader);
			bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			bufferBuilder.vertex(projectionMatrix, -10.0F, -10.0F, 0.0F).endVertex();
			bufferBuilder.vertex(projectionMatrix, 10.0F, -10.0F, 0.0F).endVertex();
			bufferBuilder.vertex(projectionMatrix, 10.0F, 10.0F, 0.0F).endVertex();
			bufferBuilder.vertex(projectionMatrix, -10.0F, 10.0F, 0.0F).endVertex();
			BufferUploader.drawWithShader(bufferBuilder.end());
		}
		
		// Finalize //

		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
		RenderSystem.enableCull();
	}
	
	private VertexBuffer buildBufferCylinder(BufferBuilder bufferBuilder, VertexBuffer buffer, double height) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);;
		makeCylinder(bufferBuilder, 16, height, 100);
		RenderedBuffer renderBuffer = bufferBuilder.end();
		buffer.bind();
		buffer.upload(renderBuffer);
		
		return buffer;
	}
	
	private void renderBuffer(PoseStack matrices, Matrix4f matrix4f, VertexBuffer buffer, VertexFormat format, float r, float g, float b, float a) {
		RenderSystem.setShaderColor(r, g, b, a);
		buffer.bind();
		if (format == DefaultVertexFormat.POSITION) {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionShader());
		}
		else {
			buffer.drawWithShader(matrices.last().pose(), matrix4f, GameRenderer.getPositionTexShader());
		}
		VertexBuffer.unbind();
	}
	
	private void makeCylinder(BufferBuilder buffer, int segments, double height, double radius) {
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
	
	private VertexBuffer buildBufferSquares(BufferBuilder bufferBuilder, VertexBuffer buffer, double minSize, double maxSize, int count, int verticalCount, long seed) {
		if (buffer != null) {
			buffer.close();
		}
		
		buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);;
		makeStars(bufferBuilder, minSize, maxSize, count, verticalCount, seed);
		RenderedBuffer renderBuffer = bufferBuilder.end();
		buffer.bind();
		buffer.upload(renderBuffer);
		
		return buffer;
	}
	
	private void makeStars(BufferBuilder buffer, double minSize, double maxSize, int count, int verticalCount, long seed) {
		RandomSource random = new XoroshiroRandomSource(seed);
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
	
	private void renderMoon(boolean front, PoseStack matrices, double orbitPosition, float orbitRadius, float orbitAngle, float size, float v0, float v1, Vector3f color) {
		float offset2 = (float) Math.cos(orbitPosition);
		if (front && offset2 < 0) return;
		if (!front && offset2 >= 0) return;
		float offset1 = (float) Math.sin(orbitPosition);
		
		matrices.pushPose();
		matrices.translate(offset1 * (70 + orbitRadius), offset1 * orbitAngle, offset2 * orbitRadius - 100);
		
		Matrix4f matrix = matrices.last().pose();
		RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1F);
		RenderSystem.setShaderTexture(0, MOON_TEXTURE);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.vertex(matrix, -size,  size, 0.0F).uv(0.0F, v0).endVertex();
		bufferBuilder.vertex(matrix,  size,  size, 0.0F).uv(1.0F, v0).endVertex();
		bufferBuilder.vertex(matrix,  size, -size, 0.0F).uv(1.0F, v1).endVertex();
		bufferBuilder.vertex(matrix, -size, -size, 0.0F).uv(0.0F, v1).endVertex();
		BufferUploader.drawWithShader(bufferBuilder.end());
		
		matrices.popPose();
	}
	
	private Vec3 getSkyColor(Vec3 pos, float tickDelta, ClientLevel level) {
		Vec3 samplePos = pos.subtract(2.0, 2.0, 2.0).scale(0.25);
		BiomeManager biomeManager = level.getBiomeManager();
		Vec3 color = CubicSampler.gaussianSampleVec3(
			samplePos,
			(i, j, k) -> Vec3.fromRGB24(biomeManager.getNoiseBiomeAtQuart(i, j, k).value().getSkyColor())
		);
		float light = Mth.cos(level.getTimeOfDay(tickDelta) * MHelper.PI2) * 2.0F + 0.5F;
		light = Mth.clamp(light, 0.0f, 1.0f);
		return color.multiply(light, light, light);
	}
}
