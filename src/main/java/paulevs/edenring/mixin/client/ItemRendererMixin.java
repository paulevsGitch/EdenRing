package paulevs.edenring.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import paulevs.edenring.client.ItemScaler;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Inject(
		method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V", shift = Shift.BEFORE),
		locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void eden_scaleItem(ItemStack itemStack, int i, int j, BakedModel bakedModel, CallbackInfo info, PoseStack poseStack) {
		if (ItemScaler.needRescale()) {
			float scale = ItemScaler.getScale();
			poseStack.scale(scale, scale, scale);
		}
	}
}
