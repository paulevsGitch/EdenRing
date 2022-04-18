package paulevs.edenring.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import paulevs.edenring.client.entities.models.DiskwingEntityModel;
import paulevs.edenring.entities.DiskwingEntity;
import paulevs.edenring.entities.DiskwingEntity.DiskwingType;
import paulevs.edenring.registries.EdenEntitiesRenderers;

@Environment(value= EnvType.CLIENT)
public class DiskwingEntityRenderer extends MobRenderer<DiskwingEntity, DiskwingEntityModel> {
	private static final RenderType[] GLOW = new RenderType[DiskwingType.VALUES.length];
	
	public DiskwingEntityRenderer(Context ctx) {
		super(ctx, new DiskwingEntityModel(ctx.bakeLayer(EdenEntitiesRenderers.DISKWING_MODEL)), 0.5F);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return GLOW[0];
			}
			
			@Override
			public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, DiskwingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				int variant = entity.getVariant().ordinal();
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[variant]);
				this.getParentModel().renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		});
	}
	
	@Override
	public ResourceLocation getTextureLocation(DiskwingEntity entity) {
		return entity.getVariant().getTexture();
	}
	
	@Override
	protected void scale(DiskwingEntity entity, PoseStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}
	
	static {
		final int length = DiskwingType.VALUES.length;
		for (byte i = 0; i < length; i++) {
			GLOW[i] = RenderType.eyes(DiskwingType.VALUES[i].getGlow());
		}
	}
}
