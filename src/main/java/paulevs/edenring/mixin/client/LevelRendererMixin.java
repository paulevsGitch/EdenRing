package paulevs.edenring.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.items.BalloonMushroomBlockItem;
import paulevs.edenring.registries.EdenBlocks;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Final @Shadow private Minecraft minecraft;
	@Shadow @Final private RenderBuffers renderBuffers;
	
	@Shadow
	private static void renderShape(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {}
	
	@Inject(method = "renderLevel", at = @At(
		value = "INVOKE",
		target = "Lcom/mojang/blaze3d/systems/RenderSystem;getModelViewStack()Lcom/mojang/blaze3d/vertex/PoseStack;",
		shift = Shift.BEFORE
	))
	public void eden_renderLevel(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo info) {
		if (minecraft.hitResult == null || !(minecraft.hitResult instanceof BlockHitResult)) {
			return;
		}
		
		ItemStack item = minecraft.player.getMainHandItem();
		if (item != null && !(item.getItem() instanceof BalloonMushroomBlockItem)) {
			return;
		}
		
		BlockPos pos = ((BlockHitResult) minecraft.hitResult).getBlockPos();
		BlockState state = minecraft.level.getBlockState(pos);
		if (!state.canBeReplaced()) return;
		
		state = EdenBlocks.BALLOON_MUSHROOM_BLOCK.defaultBlockState();
		BufferSource bufferSource = this.renderBuffers.bufferSource();
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
		Vec3 camPos = camera.getPosition();
		
		this.renderShape(
			poseStack, consumer,
			state.getShape(minecraft.level, pos, CollisionContext.of(camera.getEntity())),
			pos.getX() - camPos.x(), pos.getY() - camPos.y(), pos.getZ() - camPos.z(),
			0.7019F, 0.4549F, 0.5568F, 0.5F
		);
	}
}
