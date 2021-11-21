package paulevs.edenring.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import paulevs.edenring.EdenRing;

import java.awt.Point;
import java.util.List;

public class GuideBookScreen extends Screen {
	private static final ResourceLocation BOOK_TEXTURE = EdenRing.makeID("textures/gui/book.png");
	private static int pageIndex = 0;
	private PageInfo[] pages;
	private String pagePrefix;
	private Point arrowNext;
	private Point arrowBack;
	
	public GuideBookScreen() {
		super(NarratorChatListener.NO_TITLE);
		
		arrowNext = new Point();
		arrowBack = new Point();
		pagePrefix = getTxt("number_prefix");
		pages = new PageInfo[] {
			new PageInfo("Cover"),
			
			new PageInfo("Logo").addEntry(new CenteredIllustrationPageEntry(getImg("planet_logo"), 128, 128)),
			new PageInfo("Table Of Contents")
				.addEntry(new TitlePageEntry(getTxt("tof_line_1")))
				.addEntry(new TitlePageEntry(getTxt("tof_line_2")))
				.addEntry(new IllustrationPageEntry(getImg("separator"), 128, 16))
				.addEntry(new TextEntry(getTxt("world_structure"), getTxt("flora"), getTxt("fauna"), getTxt("materials")))
		};
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int i) {
		SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (pageIndex < pages.length - 2 && x > arrowNext.x && y > arrowNext.y && x < arrowNext.x + 16 && y < arrowNext.y + 16) {
			pageIndex = pageIndex == 0 ? 1 : pageIndex + 2;
			soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
		}
		if (pageIndex > 0 && x > arrowBack.x && y > arrowBack.y && x < arrowBack.x + 16 && y < arrowBack.y + 16) {
			pageIndex -= 2;
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
		}
		return super.mouseClicked(x, y, i);
	}
	
	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BOOK_TEXTURE);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		
		int posY = (this.height - 180) / 2;
		int arrowY = posY + (180 - 16) / 2;
		if (pageIndex <= 0) {
			int posX = (this.width - 146) / 2;
			arrowNext.setLocation(posX + 146 + 8, arrowY);
			renderImage(poseStack, posX, posY, 0, 0, 146, 180, 512, 256);
			renderImage(poseStack, arrowNext.x, arrowY, 16, 192, 16, 16, 512, 256);
		}
		else if (pageIndex + 1 >= pages.length) {
			int posX = (this.width - 146) / 2;
			arrowBack.setLocation(posX - 8 - 16, arrowY);
			renderImage(poseStack, posX, posY, 160, 0, 146, 180, 512, 256);
			renderImage(poseStack, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			pages[pageIndex].render(poseStack, posX, posY + 16, 146, 180);
		}
		else {
			int posX1 = (this.width - 146 * 2 - 8) / 2;
			int posX2 = posX1 + 146 + 8;
			
			
			arrowBack.setLocation(posX1 - 8 - 16, arrowY);
			renderImage(poseStack, posX1, posY, 160, 0, 146, 180, 512, 256);
			renderImage(poseStack, posX2, posY, 320, 0, 146, 180, 512, 256);
			renderImage(poseStack, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			
			if (pageIndex < pages.length - 2) {
				arrowNext.setLocation(posX2 + 146 + 8, arrowY);
				renderImage(poseStack, arrowNext.x, arrowY, 16, 192, 16, 16, 512, 256);
			}
			
			pages[pageIndex].render(poseStack, posX1, posY + 16, 146, 180);
			pages[pageIndex + 1].render(poseStack, posX2, posY + 16, 146, 180);
			
			posY = posY + 180 - 18;
			String number1 = pagePrefix + pageIndex;
			String number2 = pagePrefix + (pageIndex + 1);
			this.font.draw(poseStack, number1, posX1 + 24, posY, 0);
			this.font.draw(poseStack, number2, posX2 + 146 - 24 - this.font.width(number2), posY, 0);
		}
	}
	
	private void renderImage(PoseStack poseStack, int x, int y, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {
		this.blit(poseStack, x, y, u, v, width, height, atlasWidth, atlasHeight);
	}
	
	class PageInfo {
		final List<PageEntry> entires = Lists.newArrayList();
		final String name;
		
		PageInfo(String name) {
			this.name = name;
		}
		
		PageInfo addEntry(PageEntry entiry) {
			entires.add(entiry);
			return this;
		}
		
		void render(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			for (PageEntry entry: entires) {
				posY = entry.renderAndOffset(poseStack, posX, posY, pageWidth, pageHeight) + 4;
			}
		}
	}
	
	abstract class PageEntry {
		abstract int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight);
	}
	
	class TitlePageEntry extends PageEntry {
		String title;
		
		TitlePageEntry(String title) {
			this.title = title;
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			int width = GuideBookScreen.this.font.width(title);
			int posSide = posX + (pageWidth - width) / 2;
			GuideBookScreen.this.font.draw(poseStack, title, posSide, posY, 0);
			return posY + GuideBookScreen.this.font.lineHeight;
		}
	}
	
	class TextEntry extends PageEntry {
		String[] lines;
		
		TextEntry(String... lines) {
			this.lines = lines;
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			posX += 16;
			for (String line: lines) {
				GuideBookScreen.this.font.draw(poseStack, line, posX, posY, 0);
				posY += GuideBookScreen.this.font.lineHeight;
			}
			return posY;
		}
	}
	
	class IllustrationPageEntry extends PageEntry {
		final ResourceLocation texture;
		final int atlasHeight;
		final int atlasWidth;
		final int height;
		final int width;
		final int u;
		final int v;
		
		IllustrationPageEntry(ResourceLocation texture, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {
			this.texture = texture;
			this.atlasHeight = atlasHeight;
			this.atlasWidth = atlasWidth;
			this.height = height;
			this.width = width;
			this.u = u;
			this.v = v;
		}
		
		IllustrationPageEntry(ResourceLocation texture, int width, int height) {
			this(texture, 0, 0, width, height, width, height);
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			int posSide = posX + (pageWidth - width) / 2;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			renderImage(poseStack, posSide, posY, u, v, width, height, atlasWidth, atlasHeight);
			return posY + height;
		}
	}
	
	class CenteredIllustrationPageEntry extends IllustrationPageEntry {
		CenteredIllustrationPageEntry(ResourceLocation texture, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {
			super(texture, u, v, width, height, atlasWidth, atlasHeight);
		}
		
		CenteredIllustrationPageEntry(ResourceLocation texture, int width, int height) {
			super(texture, width, height);
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			int posSide = posX + (pageWidth - width) / 2;
			int posHeight = posY - 16 + (pageHeight - height) / 2;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			renderImage(poseStack, posSide, posHeight, u, v, width, height, atlasWidth, atlasHeight);
			return posY + pageHeight;
		}
	}
	
	private static ResourceLocation getImg(String name) {
		return EdenRing.makeID("textures/gui/illustrations/" + name + ".png");
	}
	
	private static String getTxt(String name) {
		Language language = Language.getInstance();
		return language.getOrDefault("gui.edenring.book." + name);
	}
}
