package paulevs.edenring.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.loader.api.FabricLoader;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.environment.renderers.EdenCloudRenderer;
import paulevs.edenring.client.environment.renderers.EdenSkyRenderer;
import paulevs.edenring.client.environment.renderers.EdenWeatherRenderer;
import paulevs.edenring.config.Configs;
import paulevs.edenring.registries.EdenBlockEntitiesRenderers;
import paulevs.edenring.registries.EdenEntitiesRenderers;
import paulevs.edenring.registries.EdenParticleFactories;

public class EdenRingClient implements ClientModInitializer {
	private static boolean hasIris;
	
	@Override
	public void onInitializeClient() {
		EdenBlockEntitiesRenderers.init();
		EdenEntitiesRenderers.init();
		EdenParticleFactories.register();
		//if (BCLib.isDevEnvironment()) {
		//	TranslationHelper.printMissingEnNames(EdenRing.MOD_ID);
		//	TranslationHelper.printMissingNames(EdenRing.MOD_ID, "ru_ru");
		//}
		DimensionRenderingRegistry.registerSkyRenderer(EdenRing.EDEN_RING_KEY, new EdenSkyRenderer());
		DimensionRenderingRegistry.registerCloudRenderer(EdenRing.EDEN_RING_KEY, new EdenCloudRenderer());
		DimensionRenderingRegistry.registerWeatherRenderer(EdenRing.EDEN_RING_KEY, new EdenWeatherRenderer());
		Configs.CLIENT_CONFIG.saveChanges();
		hasIris = FabricLoader.getInstance().isModLoaded("iris");
	}
	
	public static boolean hasIris() {
		return hasIris;
	}
}
