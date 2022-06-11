package paulevs.edenring.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.loader.api.FabricLoader;
import org.betterx.bclib.BCLib;
import org.betterx.bclib.util.TranslationHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.environment.renderers.EdenCloudRenderer;
import paulevs.edenring.client.environment.renderers.EdenSkyRenderer;
import paulevs.edenring.client.environment.renderers.EdenWeatherRenderer;
import paulevs.edenring.registries.EdenBlockEntitiesRenderers;
import paulevs.edenring.registries.EdenEntitiesRenderers;
import paulevs.edenring.registries.EdenParticles;

public class EdenRingClient implements ClientModInitializer {
	public static final EdenClientConfig CLIENT_CONFIG = new EdenClientConfig();
	private static boolean hasIris;
	
	@Override
	public void onInitializeClient() {
		EdenBlockEntitiesRenderers.init();
		EdenEntitiesRenderers.init();
		EdenParticles.register();
		if (BCLib.isDevEnvironment()) {
			TranslationHelper.printMissingEnNames(EdenRing.MOD_ID);
			TranslationHelper.printMissingNames(EdenRing.MOD_ID, "ru_ru");
		}
		DimensionRenderingRegistry.registerSkyRenderer(EdenRing.EDEN_RING_KEY, new EdenSkyRenderer());
		DimensionRenderingRegistry.registerCloudRenderer(EdenRing.EDEN_RING_KEY, new EdenCloudRenderer());
		DimensionRenderingRegistry.registerWeatherRenderer(EdenRing.EDEN_RING_KEY, new EdenWeatherRenderer());
		CLIENT_CONFIG.saveChanges();
		hasIris = FabricLoader.getInstance().isModLoaded("iris");
	}
	
	public static boolean hasIris() {
		return hasIris;
	}
}
