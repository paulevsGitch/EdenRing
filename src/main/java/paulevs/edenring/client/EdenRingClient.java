package paulevs.edenring.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlockEntitiesRenderers;
import paulevs.edenring.registries.EdenEntitiesRenderers;
import paulevs.edenring.registries.EdenParticles;
import ru.bclib.BCLib;
import ru.bclib.util.TranslationHelper;

public class EdenRingClient implements ClientModInitializer {
	public static final EdenClientConfig CLIENT_CONFIG = new EdenClientConfig();
	
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
		CLIENT_CONFIG.saveChanges();
	}
}
