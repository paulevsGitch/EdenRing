package paulevs.edenring.client.environment.animation;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.util.MHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import paulevs.edenring.client.environment.TransformHelper;
import paulevs.edenring.interfaces.BiomeCountProvider;
import paulevs.edenring.interfaces.SpriteInitializer;

import java.util.ArrayList;
import java.util.List;

public class SpriteGrid {
	private final BiomeCountProvider spriteCount;
	private final SpriteInitializer initializer;
	
	private final List<List<ChunkPos>> missingChunks = new ArrayList<>(2);
	private final SpriteChunk[][] chunks = new SpriteChunk[128][128];
	private final List<SpriteAnimation> animations = new ArrayList<>(256);
	private final RandomSource random = new XoroshiroRandomSource(0);
	private final Vector4f color = new Vector4f();
	private final Vector3f pos = new Vector3f();
	
	private ClientLevel lastLevel;
	private int lastDistance = -1;
	private byte missingIndex;
	private long lastTicks;
	private int lastX;
	private int lastZ;
	
	private float[] fogColor;
	private float fogDelta;
	private float fogStart;
	private float fogEnd;
	private float light;
	
	public SpriteGrid(SpriteInitializer initializer, BiomeCountProvider spriteCount) {
		this.initializer = initializer;
		this.spriteCount = spriteCount;
		missingChunks.add(new ArrayList<>(1024));
		missingChunks.add(new ArrayList<>(1024));
	}
	
	public void render(ClientLevel level, ChunkPos pos, int distance, PoseStack poseStack, Camera camera, float tickDelta, @Nullable Frustum frustum) {
		if (distance > 127) distance = 127;
		int half = distance >> 1;
		
		List<ChunkPos> missing = missingChunks.get(missingIndex);
		long ticks = level.getGameTime();
		if (ticks >= lastTicks) {
			lastTicks = ticks + 20;
			
			if (!missing.isEmpty()) {
				missingIndex = (byte) ((missingIndex + 1) & 1);
				List<ChunkPos> missingNew = missingChunks.get(missingIndex);
				
				missing.forEach(p -> {
					if (MHelper.max(Mth.abs(pos.x - p.x), Mth.abs(pos.z - p.z)) >= half) return;
					
					LevelChunk levelChunk = level.getChunk(p.x, p.z);
					if (levelChunk.isEmpty() && levelChunk.getPos().x == 0 && levelChunk.getPos().z == 0) {
						missingNew.add(p);
						return;
					}
					
					int dx = p.x & 127;
					int dz = p.z & 127;
					SpriteChunk chunk = chunks[dx][dz];
					
					if (chunk == null || !chunk.isCorrectPos(p.x, p.z)) {
						chunk = initChunk(level, p.x, p.z);
						chunks[dx][dz] = chunk;
					}
					animations.addAll(chunk.getPoints());
				});
				
				sort(camera);
				missing.clear();
			}
		}
		
		if (lastLevel != level || lastX != pos.x || lastZ != pos.z || lastDistance != distance) {
			animations.clear();
			lastX = pos.x;
			lastZ = pos.z;
			lastDistance = distance;
			
			if (lastLevel != level) {
				lastLevel = level;
				for (short i = 0; i < chunks.length; i++) {
					for (short j = 0; j < chunks[0].length; j++) {
						chunks[i][j] = null;
					}
				}
			}
			
			for (byte i = 0; i < distance; i++) {
				int px = pos.x - half + i;
				int dx = px & 127;
				for (byte j = 0; j < distance; j++) {
					int pz = pos.z - half + j;
					int dz = pz & 127;
					SpriteChunk chunk = chunks[dx][dz];
					if (chunk == null || !chunk.isCorrectPos(px, pz)) {
						chunk = initChunk(level, px, pz);
						if (chunk == null) {
							missing.add(new ChunkPos(px, pz));
							continue;
						}
						chunks[dx][dz] = chunk;
					}
					animations.addAll(chunk.getPoints());
				}
			}
			
			sort(camera);
		}
		
		if (animations.isEmpty()) return;
		SpriteAnimation animation = animations.get(0);
		
		float max = half * 16F;
		float min = 128F;
		if (max < min) max = min * 1.5F;
		float delta = max - min;
		Vec3 camPos = camera.getPosition();
		
		if (animation.useFogColor()) {
			fogColor = RenderSystem.getShaderFogColor();
			fogStart = RenderSystem.getShaderFogStart() * animation.fogStartMultiplier();
			fogEnd = RenderSystem.getShaderFogEnd() * animation.fogEndMultiplier();
			fogDelta = fogEnd - fogStart;
			float dayTime = level.getTimeOfDay(tickDelta);
			light = Mth.clamp((float) Math.cos(dayTime * Math.PI * 2) * 1.1F, 0.2F, 1.0F);
		}
		
		final double time = (double) level.getGameTime() + tickDelta;
		
		BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		animations.forEach(sprite -> {
			sprite.update(time);
			if (sprite.getFrame() < 0 || sprite.getAlpha() < 0.01F || sprite.getScale() < 0.01F) return;
			if (frustum != null) {
				AABB bb = sprite.getBoundingBox();
				if (bb != null && !frustum.isVisible(bb)) return;
			}
			
			BlockPos origin = sprite.getOrigin();
			float x = (float) (origin.getX() - camPos.x);
			float y = (float) (origin.getY() - camPos.y);
			float z = (float) (origin.getZ() - camPos.z);
			
			float length = MHelper.length(x, y, z);
			if (length <= min) return;
			
			float dist = (length - min) / delta;
			dist = Math.abs(dist * 2.0F - 1.0F);
			
			if (sprite.useSqrSqr()) {
				dist *= dist;
			}
			dist = 1.0F - dist * dist;
			
			float a = Mth.clamp(sprite.getAlpha() * dist * 0.75F, 0.0F, 1.0F);
			if (a < 0.01F) return;
			
			if (sprite.useFogColor()) {
				float fogMix = (length - fogStart) / fogDelta;
				if (fogMix > 1.0F) fogMix = 1.0F;
				
				float r = Mth.clamp(Mth.lerp(fogMix, light, fogColor[0] * 1.2F), 0.2F, 1.0F);
				float g = Mth.clamp(Mth.lerp(fogMix, light, fogColor[1] * 1.2F), 0.2F, 1.0F);
				float b = Mth.clamp(Mth.lerp(fogMix, light, fogColor[2] * 1.2F), 0.2F, 1.0F);
				this.color.set(r, g, b, a);
			}
			else {
				this.color.set(1.0F, 1.0F, 1.0F, a);
			}
			
			this.pos.set(x, y, z);
			sprite.offset(this.pos);
			float v = sprite.getFrame() * sprite.getVSize();
			renderSprite(sprite.getScale(), v, v + sprite.getVSize(), bufferBuilder, poseStack);
		});
		BufferUploader.drawWithShader(bufferBuilder.end());
	}
	
	private SpriteChunk initChunk(ClientLevel level, int px, int pz) {
		LevelChunk levelChunk = level.getChunk(px, pz);
		if (levelChunk.isEmpty()) return null;
		random.setSeed(MHelper.getSeed(0, px, pz));
		BCLBiome biome = BiomeAPI.getRenderBiome(levelChunk.getNoiseBiome(2, 32, 2).value());
		int count = spriteCount.getCount(biome, random);
		return new SpriteChunk(px, pz, random, count, initializer);
	}
	
	private void renderSprite(float size, float v1, float v2, BufferBuilder bufferBuilder, PoseStack poseStack) {
		poseStack.pushPose();
		TransformHelper.translateAndRotateRelative(pos.x(), pos.y(), pos.z(), poseStack);
		renderQuad(bufferBuilder, poseStack, size, v1, v2);
		poseStack.popPose();
	}
	
	private void renderQuad(BufferBuilder bufferBuilder, PoseStack poseStack, float size, float v1, float v2) {
		Matrix4f matrix = poseStack.last().pose();
		bufferBuilder.vertex(matrix, -size, -size, 0.0F).uv(0.0F, v2).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix,  size, -size, 0.0F).uv(1.0F, v2).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix,  size,  size, 0.0F).uv(1.0F, v1).color(color.x(), color.y(), color.z(), color.w()).endVertex();
		bufferBuilder.vertex(matrix, -size,  size, 0.0F).uv(0.0F, v1).color(color.x(), color.y(), color.z(), color.w()).endVertex();
	}
	
	private void sort(Camera camera) {
		BlockPos camBlockPos = camera.getBlockPosition();
		animations.sort((c1, c2) -> {
			float d1 = camBlockPos.distManhattan(c1.getOrigin());
			float d2 = camBlockPos.distManhattan(c2.getOrigin());
			return Float.compare(d2, d1);
		});
	}
}
