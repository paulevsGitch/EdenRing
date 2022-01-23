package paulevs.edenring.client.experimental;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.opengl.GL30;
import ru.bclib.BCLib;

import java.io.IOException;

public class BufferTexture extends AbstractTexture {
	private NativeImage image;
	
	public BufferTexture() {
		RenderSystem.bindTexture(getId());
		RenderSystem.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_GENERATE_MIPMAP, GL30.GL_FALSE);
		RenderSystem.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
		RenderSystem.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
		RenderSystem.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		RenderSystem.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		RenderSystem.bindTexture(0);
	}
	
	public void setSize(int width, int height) {
		RenderSystem.assertOnGameThreadOrInit();
		RenderSystem.bindTexture(getId());
		if (image == null || image.getWidth() != width || image.getHeight() != height) {
			image = new NativeImage(width, height, false);
		}
		TextureUtil.prepareImage(this.getId(), width, height);
		RenderSystem.bindTexture(0);
	}
	
	@Override
	public void load(ResourceManager resourceManager) throws IOException {}
	
	public void upload() {
		if (this.image != null) {
			this.bind();
			this.image.upload(0, 0, 0, false);
		}
		else {
			BCLib.LOGGER.warning("Trying to upload disposed texture {}", this.getId());
		}
	}
}
