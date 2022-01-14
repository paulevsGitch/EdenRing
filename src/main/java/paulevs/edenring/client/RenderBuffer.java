package paulevs.edenring.client;

import org.lwjgl.opengl.GL30;

public class RenderBuffer implements AutoCloseable {
	private final BufferTexture bufferTexture;
	private final int fbo;
	
	public RenderBuffer() {
		fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		bufferTexture = new BufferTexture();
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, bufferTexture.getId(), 0);
		unbind();
	}
	
	public void setSize(int width, int height) {
		bufferTexture.setSize(width, height);
	}
	
	public void bind() {
		bindBuffer(fbo);
	}
	
	public int getTextureID() {
		return bufferTexture.getId();
	}
	
	public static void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public static int getCurrentBuffer() {
		return GL30.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
	}
	
	public static void bindBuffer(int id) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
	}
	
	@Override
	public void close() throws Exception {
		GL30.glDeleteFramebuffers(fbo);
	}
}
