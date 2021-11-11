package paulevs.edenring;

import net.fabricmc.api.ClientModInitializer;
import paulevs.edenring.registries.EdenBlockEntitiesRenderers;
import paulevs.edenring.registries.EdenEntitiesRenderers;
import paulevs.edenring.registries.EdenParticles;
import ru.bclib.BCLib;
import ru.bclib.util.TranslationHelper;

public class EdenRingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EdenBlockEntitiesRenderers.init();
		EdenEntitiesRenderers.init();
		EdenParticles.init();
		if (BCLib.isDevEnvironment()) {
			TranslationHelper.printMissingEnNames(EdenRing.MOD_ID);
			TranslationHelper.printMissingNames(EdenRing.MOD_ID, "ru_ru");
		}
	}
}
