package paulevs.edenring.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import paulevs.edenring.blocks.entities.renderers.EdenPortalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class EdenBlockEntitiesRenderers {
	public static void init() {
		BlockEntityRendererRegistry.register(EdenBlockEntities.EDEN_PORTAL, EdenPortalBlockEntityRenderer::new);
	}
}
