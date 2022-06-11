package paulevs.edenring.client;

import org.betterx.bclib.config.ConfigUI;
import org.betterx.bclib.config.NamedPathConfig;
import paulevs.edenring.EdenRing;

public class EdenClientConfig extends NamedPathConfig {
	@ConfigUI
	private static final ConfigToken<Boolean> RENDER_SKY = ConfigToken.Boolean(true, "renderSky", "rendering");
	
	@ConfigUI
	private static final ConfigToken<Boolean> RENDER_BUFFER = ConfigToken.Boolean(false, "renderInBuffer", "rendering");
	
	public EdenClientConfig() {
		super(EdenRing.MOD_ID, "client", false, false);
		getBoolean(RENDER_SKY, RENDER_SKY.defaultValue);
		getBoolean(RENDER_BUFFER, RENDER_BUFFER.defaultValue);
	}
	
	public boolean renderSky() {
		return get(RENDER_SKY);
	}
	
	public boolean renderInBuffer() {
		return get(RENDER_BUFFER);
	}
}
