package paulevs.edenring.client.environment.clouds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import paulevs.edenring.client.environment.TransformHelper;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudGrid {
	private final CloudChunk[][] chunks = new CloudChunk[128][128];
	private final List<CloudAnimation> clouds = new ArrayList<>(256);
	private final VertexBuffer[] cloudQuads = new VertexBuffer[4];
	private final Random random = new Random();
	private final Vector4f color = new Vector4f();
	private final Vector3f pos = new Vector3f();
	private int lastDistance = -1;
	private int lastX;
	private int lastZ;
	
	public void render(ClientLevel level, ChunkPos pos, int distance, PoseStack poseStack, Camera camera, float tickDelta, Frustum frustum) {
		if (distance > 127) distance = 127;
		int half = distance >> 1;
		
		if (lastX != pos.x || lastZ != pos.z || lastDistance != distance) {
			clouds.clear();
			lastX = pos.x;
			lastZ = pos.z;
			lastDistance = distance;
			
			if (cloudQuads[0] == null) {
				BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
				for (byte i = 0; i < 4; i++) {
					float v1 = i * 0.25F;
					float v2 = v1 + 0.25F;
					
					bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
					bufferBuilder.vertex(-1.0F, -1.0F, 0.0F).uv(0.0F, v2).endVertex();
					bufferBuilder.vertex( 1.0F, -1.0F, 0.0F).uv(1.0F, v2).endVertex();
					bufferBuilder.vertex( 1.0F,  1.0F, 0.0F).uv(1.0F, v1).endVertex();
					bufferBuilder.vertex(-1.0F,  1.0F, 0.0F).uv(0.0F, v1).endVertex();
					bufferBuilder.end();
					
					cloudQuads[i] = new VertexBuffer();
					cloudQuads[i].upload(bufferBuilder);
				}
			}
			
			for (byte i = 0; i < distance; i++) {
				int px = pos.x - half + i;
				int dx = px & 63;
				for (byte j = 0; j < distance; j++) {
					int pz = pos.z - half + j;
					int dz = pz & 63;
					CloudChunk chunk = chunks[dx][dz];
					if (chunk == null || !chunk.isCorrectPos(px, pz)) {
						random.setSeed(MHelper.getSeed(0, px, pz));
						int count = random.nextInt(16) == 0 ? 1 : 0;
						
						BCLBiome biome = BiomeAPI.getRenderBiome(level.getChunk(px, pz).getNoiseBiome(0, 0, 0).value());
						float fog = biome.getFogDensity();
						if (fog > 1 && random.nextInt(5) > 0) {
							count = (int) (random.nextFloat() * fog * 2);
						}
						
						chunk = new CloudChunk(px, pz, random, count);
						chunks[dx][dz] = chunk;
					}
					clouds.addAll(chunk.getPoints());
				}
			}
			
			BlockPos camBlockPos = camera.getBlockPosition();
			clouds.sort((c1, c2) -> {
				float d1 = camBlockPos.distManhattan(c1.getOrigin());
				float d2 = camBlockPos.distManhattan(c2.getOrigin());
				return Float.compare(d2, d1);
			});
		}
		
		float max = half * 16F;
		float min = 128F;
		if (max < min) max = min * 1.2F;
		float delta = max - min;
		Vec3 camPos = camera.getPosition();
		
		float[] fogColor = RenderSystem.getShaderFogColor();
		float fogStart = RenderSystem.getShaderFogStart();
		float fogEnd = RenderSystem.getShaderFogEnd() * 1.5F;
		float fogDelta = fogEnd - fogStart;
		
		float dayTime = level.getTimeOfDay(tickDelta);
		final float light = Mth.clamp((float) Math.cos(dayTime * Math.PI * 2) * 1.1F, 0.2F, 1.0F);
		final double time = (double) level.getGameTime() + tickDelta;
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		clouds.forEach(cloud -> {
			cloud.update(time);
			if (cloud.getAlpha() < 0.01F || cloud.getScale() < 0.01F) return;
			if (!frustum.isVisible(cloud.getBoundingBox())) return;
			
			BlockPos origin = cloud.getOrigin();
			float x = (float) (origin.getX() - camPos.x);
			float y = (float) (origin.getY() - camPos.y);
			float z = (float) (origin.getZ() - camPos.z);
			
			float length = MHelper.length(x, y, z);
			if (length <= min) return;
			
			float dist = (length - min) / delta;
			dist = Math.abs(dist * 2.0F - 1.0F);
			
			dist *= dist;
			dist = 1.0F - dist * dist;
			
			float a = Mth.clamp(cloud.getAlpha() * dist * 0.75F, 0.0F, 1.0F);
			if (a < 0.01F) return;
			
			float fogMix = (length - fogStart) / fogDelta;
			if (fogMix > 1.0F) fogMix = 1.0F;
			
			float r = Mth.clamp(Mth.lerp(fogMix, light, fogColor[0] * 1.2F), 0.2F, 1.0F);
			float g = Mth.clamp(Mth.lerp(fogMix, light, fogColor[1] * 1.2F), 0.2F, 1.0F);
			float b = Mth.clamp(Mth.lerp(fogMix, light, fogColor[2] * 1.2F), 0.2F, 1.0F);
			
			float v = cloud.getIndex() * 0.25F;
			this.pos.set(x + cloud.getOffset(), y, z);
			this.color.set(r, g, b, a);
			renderSprite(cloud.getScale(), v, v + 0.25F, bufferBuilder, poseStack);
		});
		bufferBuilder.end();
		BufferUploader.end(bufferBuilder);
	}
	
	protected void renderSprite(float size, float v1, float v2, BufferBuilder bufferBuilder, PoseStack poseStack) {
		poseStack.pushPose();
		TransformHelper.translateAndRotateRelative(pos.x(), pos.y(), pos.z(), poseStack);
		renderQuad(bufferBuilder, poseStack, size, v1, v2);
		poseStack.popPose();
	}
	
	protected void renderQuad(BufferBuilder bufferBuilder, PoseStack poseStack, float size, float v1, float v2) {
		Matrix4f matrix = poseStack.last().pose();
		bufferBuilder.vertex(matrix, -size, -size, 0.0F).uv(0.0F, v2).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix,  size, -size, 0.0F).uv(1.0F, v2).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix,  size,  size, 0.0F).uv(1.0F, v1).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix, -size,  size, 0.0F).uv(0.0F, v1).color(color.x(), color.y(), color.z(), color.w()).endVertex();
	}
}
