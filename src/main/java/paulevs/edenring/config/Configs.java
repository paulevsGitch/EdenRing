package paulevs.edenring.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.betterx.bclib.BCLib;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;
import org.betterx.bclib.config.PathConfig;
import paulevs.edenring.EdenRing;
import paulevs.edenring.client.EdenClientConfig;

public class Configs {
    public static final IdConfig BIOMES = new EntryConfig(EdenRing.MOD_ID, "biomes");
    public static final PathConfig GENERATOR = new PathConfig(EdenRing.MOD_ID, "generator", false, false);
    public static final PathConfig ITEMS = new PathConfig(EdenRing.MOD_ID, "items");
    public static final PathConfig RECIPES = new PathConfig(EdenRing.MOD_ID, "recipes");

    @Environment(EnvType.CLIENT)
    public static final EdenClientConfig CLIENT_CONFIG = new EdenClientConfig();

    public static void saveConfigs() {
        BIOMES.saveChanges();
        GENERATOR.saveChanges();
        ITEMS.saveChanges();
        RECIPES.saveChanges();

        if (BCLib.isClient()) {
            CLIENT_CONFIG.saveChanges();
        }
    }
}
