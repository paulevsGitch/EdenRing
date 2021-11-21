package paulevs.edenring.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import paulevs.edenring.EdenRing;

public class GuideBookScreen extends Screen {
	private static final ResourceLocation BOOK_TEXTURE = EdenRing.makeID("textures/gui/book.png");
	private static int pageIndex = 1;
	
	public GuideBookScreen() {
		super(NarratorChatListener.NO_TITLE);
	}
	
	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
		RenderSystem.disableCull();
		
		int posY = (this.height - 180) / 2;
		if (pageIndex == 0) {
			int posX = (this.width - 146) / 2;
			renderImage(poseStack, posX, posY, 0, 0, 146, 180, 512, 256);
		}
		else {
			int posX = (this.width - 146 * 2 - 8) / 2;
			renderImage(poseStack, posX, posY, 160, 0, 146, 180, 512, 256);
			renderImage(poseStack, posX + 146 + 8, posY, 320, 0, 146, 180, 512, 256);
		}
		
		RenderSystem.enableCull();
	}
	
	private void renderImage(PoseStack poseStack, int x, int y, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {
		this.blit(poseStack, x, y, u, v, width, height, atlasWidth, atlasHeight);
	}
}
