package paulevs.edenring.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import paulevs.edenring.EdenRing;
import ru.bclib.util.JsonFactory;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GuideBookScreen extends Screen {
	private static final Map<String, Function<JsonObject, PageEntry>> ENTRY_REGISTRY = Maps.newHashMap();
	private static final ResourceLocation BOOK_TEXTURE = EdenRing.makeID("textures/gui/book.png");
	private static int pageIndex = 0;
	
	private final Map<String, BookInfo> booksCache = Maps.newHashMap();
	private final Map<String, PageInfo> pageByName = Maps.newHashMap();
	private final BookInfo book;
	private final Point arrowNext;
	private final Point arrowBack;
	
	public GuideBookScreen() {
		super(NarratorChatListener.NO_TITLE);
		
		if (ENTRY_REGISTRY.isEmpty()) {
			ENTRY_REGISTRY.put("text", TextPageEntry::new);
			ENTRY_REGISTRY.put("title", TitlePageEntry::new);
			ENTRY_REGISTRY.put("illustration", IllustrationPageEntry::new);
		}
		
		arrowNext = new Point();
		arrowBack = new Point();
		
		String code = Minecraft.getInstance().getLanguageManager().getSelected().getCode();
		book = getBook(code);
	}
	
	private BookInfo getBook(String code) {
		BookInfo book = booksCache.get(code);
		
		if (book != null) {
			return book;
		}
		
		JsonObject obj = JsonFactory.getJsonObject(EdenRing.makeID("lang/book/" + code + ".json"));
		
		if (obj == null) {
			obj = JsonFactory.getJsonObject(EdenRing.makeID("lang/book/en_us.json"));
		}
		
		String prefix = obj.get("lineNumberPrefix").getAsString();
		JsonArray pages = obj.getAsJsonArray("pages");
		
		PageInfo[] bookPages = new PageInfo[pages.size() + 1];
		for (int i = 0; i < pages.size(); i++) {
			bookPages[i + 1] = new PageInfo(pages.get(i).getAsJsonObject());
		}
		
		book = new BookInfo(prefix, bookPages);
		booksCache.put(code, book);
		return book;
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int i) {
		SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (pageIndex < book.pages.length - 2 && x > arrowNext.x && y > arrowNext.y && x < arrowNext.x + 16 && y < arrowNext.y + 16) {
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
		else if (pageIndex + 1 >= book.pages.length) {
			int posX = (this.width - 146) / 2;
			arrowBack.setLocation(posX - 8 - 16, arrowY);
			renderImage(poseStack, posX, posY, 160, 0, 146, 180, 512, 256);
			renderImage(poseStack, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			book.pages[pageIndex].render(poseStack, posX, posY + 16, 146, 180);
		}
		else {
			int posX1 = (this.width - 146 * 2 - 8) / 2;
			int posX2 = posX1 + 146 + 8;
			
			arrowBack.setLocation(posX1 - 8 - 16, arrowY);
			renderImage(poseStack, posX1, posY, 160, 0, 146, 180, 512, 256);
			renderImage(poseStack, posX2, posY, 320, 0, 146, 180, 512, 256);
			renderImage(poseStack, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			
			if (pageIndex < book.pages.length - 2) {
				arrowNext.setLocation(posX2 + 146 + 8, arrowY);
				renderImage(poseStack, arrowNext.x, arrowY, 16, 192, 16, 16, 512, 256);
			}
			
			book.pages[pageIndex].render(poseStack, posX1, posY + 16, 146, 180);
			book.pages[pageIndex + 1].render(poseStack, posX2, posY + 16, 146, 180);
			
			posY = posY + 180 - 18;
			String number1 = book.numberPrefix + pageIndex;
			String number2 = book.numberPrefix + (pageIndex + 1);
			this.font.draw(poseStack, number1, posX1 + 24, posY, 0);
			this.font.draw(poseStack, number2, posX2 + 146 - 24 - this.font.width(number2), posY, 0);
		}
	}
	
	private void renderImage(PoseStack poseStack, int x, int y, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {
		this.blit(poseStack, x, y, u, v, width, height, atlasWidth, atlasHeight);
	}
	
	class BookInfo {
		String numberPrefix;
		PageInfo[] pages;
		
		BookInfo(String numberPrefix, PageInfo[] pages) {
			this.numberPrefix = numberPrefix;
			this.pages = pages;
		}
	}
	
	class PageInfo {
		final List<PageEntry> entries = Lists.newArrayList();
		final String name;
		
		PageInfo(JsonObject obj) {
			JsonElement element = obj.get("name");
			name = element != null ? element.getAsString() : null;
			if (name != null) {
				pageByName.put(name, this);
			}
			element = obj.get("entries");
			if (element != null && element instanceof JsonArray) {
				element.getAsJsonArray().forEach(entry -> {
					JsonObject object = entry.getAsJsonObject();
					String type = object.get("type").getAsString();
					Function<JsonObject, PageEntry> init = ENTRY_REGISTRY.get(type);
					if (init != null) {
						entries.add(init.apply(object));
					}
				});
			}
		}
		
		PageInfo addEntry(PageEntry entiry) {
			entries.add(entiry);
			return this;
		}
		
		void render(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			for (PageEntry entry: entries) {
				posY = entry.renderAndOffset(poseStack, posX, posY, pageWidth, pageHeight) + 4;
			}
		}
	}
	
	abstract class PageEntry {
		PageEntry(JsonObject obj) {}
		
		abstract int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight);
	}
	
	class TitlePageEntry extends PageEntry {
		String title;
		
		TitlePageEntry(JsonObject obj) {
			super(obj);
			title = obj.get("title").getAsString();
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			int width = GuideBookScreen.this.font.width(title);
			int posSide = posX + (pageWidth - width) / 2;
			GuideBookScreen.this.font.draw(poseStack, title, posSide, posY, 0);
			return posY + GuideBookScreen.this.font.lineHeight;
		}
	}
	
	class TextPageEntry extends PageEntry {
		String[] lines;
		
		TextPageEntry(JsonObject obj) {
			super(obj);
			JsonArray preLines = obj.getAsJsonArray("lines");
			lines = new String[preLines.size()];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = preLines.get(i).getAsString();
			}
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
		final boolean centered;
		final int height;
		final int width;
		
		IllustrationPageEntry(JsonObject obj) {
			super(obj);
			this.texture = new ResourceLocation(obj.get("image").getAsString());
			height = obj.get("height").getAsInt();
			width = obj.get("width").getAsInt();
			JsonElement preCentered = obj.get("centered");
			centered = preCentered == null ? false : preCentered.getAsBoolean();
		}
		
		@Override
		int renderAndOffset(PoseStack poseStack, int posX, int posY, int pageWidth, int pageHeight) {
			int posSide = posX + (pageWidth - width) / 2;
			int posHeight = centered ? posY - 16 + (pageHeight - height) / 2 : posY;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			renderImage(poseStack, posSide, posHeight, 0, 0, width, height, width, height);
			return posY + height;
		}
	}
}
