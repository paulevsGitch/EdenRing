package paulevs.edenring.registries;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.EntityType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.entities.renderers.LightningRayRenderer;

import java.util.function.Function;

public class EdenEntitiesRenderers {
	public static final ModelLayerLocation LIGHTNING_RAY_MODEL = registerMain("lightning_ray");
	
	public static void init() {
		register(EdenEntities.LIGHTNING_RAY, LightningRayRenderer::new);
	}
	
	private static void register(EntityType<?> type, Function<Context, ? extends EntityRenderer> renderer) {
		EntityRendererRegistry.register(type, (context) -> renderer.apply(context));
	}
	
	private static ModelLayerLocation registerMain(String id) {
		return new ModelLayerLocation(EdenRing.makeID(id), "main");
	}
}
