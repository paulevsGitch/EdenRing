package paulevs.edenring.registries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.EntityType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.entities.models.DiskwingEntityModel;
import paulevs.edenring.client.entities.renderers.DiskwingEntityRenderer;
import paulevs.edenring.client.entities.renderers.EdenPaintingRenderer;
import paulevs.edenring.client.entities.renderers.LightningRayRenderer;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class EdenEntitiesRenderers {
	public static final ModelLayerLocation LIGHTNING_RAY_MODEL = registerMain("lightning_ray");
	public static final ModelLayerLocation DISKWING_MODEL = registerMain("diskwing");
	
	public static void init() {
		register(EdenEntities.LIGHTNING_RAY, LightningRayRenderer::new);
		register(EdenEntities.LIMPHIUM_PAINTING, EdenPaintingRenderer::new);
		register(EdenEntities.DISKWING, DiskwingEntityRenderer::new);
		
		EntityModelLayerRegistry.registerModelLayer(DISKWING_MODEL, DiskwingEntityModel::getTexturedModelData);
	}
	
	private static void register(EntityType<?> type, Function<Context, ? extends EntityRenderer> renderer) {
		EntityRendererRegistry.register(type, (context) -> renderer.apply(context));
	}
	
	private static ModelLayerLocation registerMain(String id) {
		return new ModelLayerLocation(EdenRing.makeID(id), "main");
	}
}
