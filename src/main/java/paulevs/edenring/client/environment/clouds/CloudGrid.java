package paulevs.edenring.client.environment.clouds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
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
	private final Random random = new Random();
	private int lastDistance = -1;
	private int lastX;
	private int lastZ;
	
	public void render(ClientLevel level, ChunkPos pos, int distance, BufferBuilder bufferBuilder, PoseStack poseStack, Camera camera, double time) {
		if (distance > 127) distance = 127;
		int half = distance >> 1;
		
		if (lastX != pos.x || lastZ != pos.z || lastDistance != distance) {
			clouds.clear();
			lastX = pos.x;
			lastZ = pos.z;
			lastDistance = distance;
			
			for (byte i = 0; i < distance; i++) {
				int px = pos.x - half + i;
				int dx = px & 63;
				for (byte j = 0; j < distance; j++) {
					int pz = pos.z - half + j;
					int dz = pz & 63;
					CloudChunk chunk = chunks[dx][dz];
					if (chunk == null || !chunk.isCorrectPos(px, pz)) {
						random.setSeed(MHelper.getSeed(0, px, pz));
						int count = random.nextInt(4) == 0 ? 1 : 0;
						
						BCLBiome biome = BiomeAPI.getRenderBiome(level.getChunk(px, pz).getNoiseBiome(0, 0, 0).value());
						float fog = biome.getFogDensity();
						if (fog > 1) {
							//count = random.nextBoolean() ? 0 : (int) (random.nextFloat() * fog * 1.5F);
							count =(int) (random.nextFloat() * fog * 3F);
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
		float min = 90F;
		if (max < min) return;
		float delta = max - min;
		Vec3 camPos = camera.getPosition();
		clouds.forEach(cloud -> {
			cloud.update(time);
			if (cloud.getAlpha() < 0.01F) return;
			BlockPos origin = cloud.getOrigin();
			double x = origin.getX() - camPos.x;
			double y = origin.getY() - camPos.y;
			double z = origin.getZ() - camPos.z;
			float dist = ((float) MHelper.length(x, y, z) - min) / delta;
			if (dist > 0.95F) return;
			dist = Math.abs(dist * 2F - 1F);
			if (dist < 0.01F) return;
			dist *= dist;
			dist = 1.0F - dist * dist;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, cloud.getAlpha() * dist);
			float v = cloud.getIndex() * 0.25F;
			renderSprite(x + cloud.getOffset(), y, z, cloud.getScale(), v, v + 0.25F, bufferBuilder, poseStack);
		});
	}
	
	protected void renderSprite(double x, double y, double z, float size, float v1, float v2, BufferBuilder bufferBuilder, PoseStack poseStack) {
		poseStack.pushPose();
		TransformHelper.translateAndRotateRelative(x, y, z, poseStack);
		renderQuad(bufferBuilder, poseStack, size, v1, v2);
		poseStack.popPose();
	}
	
	protected void renderQuad(BufferBuilder bufferBuilder, PoseStack poseStack, float size, float v1, float v2) {
		Matrix4f matrix = poseStack.last().pose();
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
		bufferBuilder.vertex(matrix, -size, -size, 0.0F).uv(0.0F, v2).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		bufferBuilder.vertex(matrix,  size, -size, 0.0F).uv(1.0F, v2).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		bufferBuilder.vertex(matrix,  size,  size, 0.0F).uv(1.0F, v1).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		bufferBuilder.vertex(matrix, -size,  size, 0.0F).uv(0.0F, v1).color(255, 255, 255, 255).normal(0, 1, 0).endVertex();
		bufferBuilder.end();
		BufferUploader.end(bufferBuilder);
	}
}
