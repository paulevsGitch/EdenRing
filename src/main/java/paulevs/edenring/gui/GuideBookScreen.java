package paulevs.edenring.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import org.betterx.bclib.util.JsonFactory;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.ItemScaler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class GuideBookScreen extends Screen {
	private static final Map<String, Function<JsonObject, PageEntry>> ENTRY_REGISTRY = Maps.newHashMap();
	private static final ResourceLocation BOOK_TEXTURE = EdenRing.makeID("textures/gui/book.png");
	private static final Map<String, BookInfo> BOOKS_CACHE = Maps.newHashMap();
	private static final Stack<Integer> BACK_PAGES = new Stack<>();
	private static int pageIndex = 0;
	
	private final Minecraft minecraft;
	private final Point arrowToHyper;
	private final Point arrowNext;
	private final Point arrowBack;
	private final BookInfo book;
	
	public GuideBookScreen() {
		super(Component.empty());
		
		if (ENTRY_REGISTRY.isEmpty()) {
			ENTRY_REGISTRY.put("text", TextPageEntry::new);
			ENTRY_REGISTRY.put("title", TitlePageEntry::new);
			ENTRY_REGISTRY.put("hyperText", HyperTextPageEntry::new);
			ENTRY_REGISTRY.put("illustration", IllustrationPageEntry::new);
			ENTRY_REGISTRY.put("icon", IconPageEntry::new);
		}
		
		arrowToHyper = new Point();
		arrowNext = new Point();
		arrowBack = new Point();
		
		minecraft = Minecraft.getInstance();
		String code = minecraft.getLanguageManager().getSelected();
		book = getBook(code);
	}
	
	public static void clearCache() {
		BOOKS_CACHE.clear();
		BACK_PAGES.clear();
	}
	
	private BookInfo getBook(String code) {
		BookInfo book = BOOKS_CACHE.get(code);
		
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
		BOOKS_CACHE.put(code, book);
		return book;
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int i) {
		SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (pageIndex < book.pages.length - 2 && x > arrowNext.x && y > arrowNext.y && x < arrowNext.x + 16 && y < arrowNext.y + 16) {
			pageIndex = pageIndex == 0 ? 1 : pageIndex + 2;
			soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
		}
		if (pageIndex > 0) {
			if (x > arrowBack.x && y > arrowBack.y && x < arrowBack.x + 16 && y < arrowBack.y + 16) {
				pageIndex -= 2;
				if (pageIndex < 0) {
					pageIndex = 0;
				}
				soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
			}
			else if (!BACK_PAGES.isEmpty() && x > arrowToHyper.x && y > arrowToHyper.y && x < arrowToHyper.x + 16 && y < arrowToHyper.y + 16) {
				Integer index = BACK_PAGES.pop();
				soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
				pageIndex = index.intValue();
			}
			else {
				for (byte j = 0; j < 2; j++) {
					int index = pageIndex + j;
					if (index < book.pages.length && book.pages[index] != null) {
						book.pages[index].entries.forEach(pageEntry -> {
							if (pageEntry instanceof HyperTextPageEntry) {
								HyperTextPageEntry hyperText = (HyperTextPageEntry) pageEntry;
								for (byte n = 0; n < hyperText.start.length; n++) {
									Point start = hyperText.start[n];
									if (start == null) continue;
									Point size = hyperText.size[n];
									float px = (float) (x - start.x);
									float py = (float) (y - start.y);
									if (px >= 0 && py >= 0 && px < size.x && py < size.y) {
										if (hyperText.pages[n] == -1) break;
										soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
										BACK_PAGES.push(Integer.valueOf(pageIndex));
										pageIndex = hyperText.pages[n];
										break;
									}
								}
							}
						});
					}
				}
			}
		}
		return super.mouseClicked(x, y, i);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		PoseStack poseStack = guiGraphics.pose();
		this.renderBackground(guiGraphics);
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
			renderImage(guiGraphics, posX, posY, 0, 0, 146, 180, 512, 256);
			renderImage(guiGraphics, arrowNext.x, arrowY, 16, 192, 16, 16, 512, 256);
		}
		else if (pageIndex + 1 >= book.pages.length) {
			int posX = (this.width - 146) / 2;
			arrowBack.setLocation(posX - 8 - 16, arrowY);
			arrowToHyper.setLocation(arrowBack);
			arrowToHyper.y = posY + 8;
			renderImage(guiGraphics, posX, posY, 160, 0, 146, 180, 512, 256);
			renderImage(guiGraphics, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			if (!BACK_PAGES.empty()) renderImage(guiGraphics, arrowToHyper.x, arrowToHyper.y, 0, 208, 16, 16, 512, 256);
			book.pages[pageIndex].render(guiGraphics, posX, posY + 16, 146, 180);
			
			posY = posY + 180 - 18;
			String number = book.numberPrefix + pageIndex;
			guiGraphics.drawString(this.font, number, posX + 24, posY, 0);
		}
		else {
			int posX1 = (this.width - 146 * 2 - 8) / 2;
			int posX2 = posX1 + 146 + 8;
			
			arrowBack.setLocation(posX1 - 8 - 16, arrowY);
			arrowToHyper.setLocation(arrowBack);
			arrowToHyper.y = posY + 8;
			renderImage(guiGraphics, posX1, posY, 160, 0, 146, 180, 512, 256);
			renderImage(guiGraphics, posX2, posY, 320, 0, 146, 180, 512, 256);
			renderImage(guiGraphics, arrowBack.x, arrowY, 0, 192, 16, 16, 512, 256);
			if (!BACK_PAGES.empty()) renderImage(guiGraphics, arrowToHyper.x, arrowToHyper.y, 0, 208, 16, 16, 512, 256);
			
			if (pageIndex < book.pages.length - 2) {
				arrowNext.setLocation(posX2 + 146 + 8, arrowY);
				renderImage(guiGraphics, arrowNext.x, arrowY, 16, 192, 16, 16, 512, 256);
			}
			
			book.pages[pageIndex].render(guiGraphics, posX1, posY + 16, 146, 180);
			book.pages[pageIndex + 1].render(guiGraphics, posX2 - 6, posY + 16, 146, 180);
			
			posY = posY + 180 - 18;
			String number1 = book.numberPrefix + pageIndex;
			String number2 = book.numberPrefix + (pageIndex + 1);
			guiGraphics.drawString(this.font, number1, posX1 + 24, posY, 0);
			guiGraphics.drawString(this.font, number2, posX2 + 146 - 24 - this.font.width(number2), posY, 0);
		}
	}
	
	private void renderImage(GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height, int atlasWidth, int atlasHeight) {

		guiGraphics.blit(BOOK_TEXTURE, x, y, u, v, width, height, atlasWidth, atlasHeight);
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
		
		void render(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			for (PageEntry entry: entries) {
				posY = entry.renderAndOffset(guiGraphics, posX + 2, posY, pageWidth, pageHeight) + 4;
			}
		}
	}
	
	abstract class PageEntry {
		PageEntry(JsonObject obj) {}
		
		abstract int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight);
	}
	
	class TitlePageEntry extends PageEntry {
		String title;
		
		TitlePageEntry(JsonObject obj) {
			super(obj);
			title = obj.get("title").getAsString();
		}
		
		@Override
		int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			int width = GuideBookScreen.this.font.width(title);
			int posSide = posX + (pageWidth - width) / 2;
			guiGraphics.drawString(GuideBookScreen.this.font, title, posSide, posY, 0);
			return posY + GuideBookScreen.this.font.lineHeight;
		}
	}

	class TextPageEntry extends PageEntry {
		boolean requireRecombination = false;
		String[] lines;
		
		TextPageEntry(JsonObject obj) {
			super(obj);
			JsonElement element = obj.get("text");
			if (element.isJsonArray()) {
				JsonArray preLines = obj.getAsJsonArray("text");
				lines = new String[preLines.size()];
				for (int i = 0; i < lines.length; i++) {
					lines[i] = preLines.get(i).getAsString();
				}
			}
			else {
				requireRecombination = true;
				String text = element.getAsString();
				lines = text.split(" ");
			}
		}
		
		@Override
		int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			posX += 16;
			if (requireRecombination) {
				String modifier = null;
				requireRecombination = false;
				if (lines[0].startsWith("§")) {
					modifier = lines[0].substring(0, 2);
				}
				boolean run = true;
				List<String> newLines = new ArrayList<>(lines.length >> 2);
				for (int i = 0; run && i < lines.length; ++i) {
					int length = lines[i].length();
					//int count = 1;
					
					StringBuilder builder = new StringBuilder();
					if (modifier != null) {
						if (i == 0) length -= 2;
						else builder.append(modifier);
					}
					builder.append(lines[i]);
					
					for (int j = i + 1; j < lines.length; ++j) {
						String word = lines[j];
						length += word.length() + 1;
						if (length < 22) {
							builder.append(' ');
							builder.append(word);
							run = (j != lines.length - 1);
							//count++;
						}
						else {
							//length -= word.length() + 1;
							i = j - 1;
							break;
						}
					}
					
					String line = builder.toString();
					/*if (count > 1) {
						int spaces = 22 - length;
						if (spaces > 0 && spaces < 11) {
							int searchIndex = 0;
							for (byte n = 0; n < spaces; n++) {
								searchIndex = line.indexOf(' ', searchIndex + 2);
								if (searchIndex < 0) searchIndex = line.indexOf(' ');
								line = line.substring(0, searchIndex) + ' ' + line.substring(searchIndex);
							}
						}
					}*/
					newLines.add(line);
				}
				lines = newLines.toArray(new String[newLines.size()]);
			}
			for (String line: lines) {
				guiGraphics.drawString(GuideBookScreen.this.font, line, posX, posY, 0);
				posY += GuideBookScreen.this.font.lineHeight;
			}
			return posY;
		}
	}
	
	class HyperTextPageEntry extends PageEntry {
		String[] lines;
		String[] keys;
		Point[] start;
		Point[] size;
		int[] pages;
		
		HyperTextPageEntry(JsonObject obj) {
			super(obj);
			JsonArray preLines = obj.getAsJsonArray("lines");
			lines = new String[preLines.size()];
			start = new Point[lines.length];
			size = new Point[lines.length];
			keys = new String[lines.length];
			pages = new int[lines.length];
			for (int i = 0; i < lines.length; i++) {
				obj = preLines.get(i).getAsJsonObject();
				lines[i] = obj.get("text").getAsString();
				keys[i] = obj.get("link").getAsString();
			}
		}
		
		@Override
		int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			posX += 16;
			for (byte i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (start[i] == null) {
					int width = GuideBookScreen.this.font.width(line);
					int height = GuideBookScreen.this.font.lineHeight;
					start[i] = new Point(posX, posY);
					size[i] = new Point(width, height);
					
					String key = keys[i];
					pages[i] = -1;
					for (int n = 1; n < book.pages.length; n++) {
						PageInfo info = book.pages[n];
						if (info != null && info.name != null && info.name.equals(key)) {
							pages[i] = (n & 1) == 1 ? n : n - 1;
							break;
						}
					}
				}
				start[i].setLocation(posX, posY);
				guiGraphics.drawString(GuideBookScreen.this.font, line, posX, posY, 0);
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
		int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			int posSide = posX + (pageWidth - width) / 2;
			int posHeight = centered ? posY - 16 + (pageHeight - height) / 2 : posY;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, texture);
			renderImage(guiGraphics, posSide, posHeight, 0, 0, width, height, width, height);
			return posY + height;
		}
	}
	
	class IconPageEntry extends PageEntry {
		final private ItemStack item;
		final private int height;
		final boolean centered;
		
		IconPageEntry(JsonObject obj) {
			super(obj);
			String text = obj.get("item").getAsString();
			height = obj.get("height").getAsInt();
			ResourceLocation location = new ResourceLocation(text);
			item = new ItemStack(BuiltInRegistries.ITEM.get(location));
			JsonElement preCentered = obj.get("centered");
			centered = preCentered != null && preCentered.getAsBoolean();
		}
		
		@Override
		int renderAndOffset(GuiGraphics guiGraphics, int posX, int posY, int pageWidth, int pageHeight) {
			int posSide = posX + (pageWidth - 16) / 2;
			int posHeight = centered ? posY + (pageHeight) / 2 + 16 : posY + 16;
			if (height != 16) ItemScaler.setScale(height);
			guiGraphics.renderItem(item, posSide, posHeight);
			return posY + height;
		}
	}
}
