package paulevs.edenring.paintings;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PaintingInfo {
	private final PaintingColorProvider provider;
	private final ResourceLocation texture;
	private final ResourceLocation id;
	private final int width;
	private final int height;
	private final int cells;
	private final int rawID;
	
	public PaintingInfo(int rawID, ResourceLocation id, ResourceLocation texture, int width, int height, @Nullable PaintingColorProvider provider) {
		this.provider = provider;
		this.texture = texture;
		this.height = height;
		this.width = width;
		this.rawID = rawID;
		this.id = id;
		this.cells = (width >> 4) * (height >> 4);
	}
	
	@Nullable
	public PaintingColorProvider getProvider() {
		return provider;
	}
	
	public ResourceLocation getId() {
		return id;
	}
	
	public int getRawID() {
		return rawID;
	}
	
	public ResourceLocation getTexture() {
		return texture;
	}
	
	public int getCells() {
		return cells;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
