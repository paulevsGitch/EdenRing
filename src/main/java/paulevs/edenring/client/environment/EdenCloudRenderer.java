package paulevs.edenring.client.environment;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry.CloudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

@Environment(value= EnvType.CLIENT)
public class EdenCloudRenderer implements CloudRenderer {
	@Override
	public void render(WorldRenderContext context) {}
}
