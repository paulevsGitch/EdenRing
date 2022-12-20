package paulevs.edenring.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import paulevs.edenring.EdenRing;

public class EdenSounds {
	public static final SoundEvent BLOCK_ELECTRIC = register("block", "electric");
	public static final Holder<SoundEvent> MUSIC_COMMON = registerForHolder("music", "common");
	
	public static final Holder<SoundEvent> AMBIENCE_BRAINSTORM = registerForHolder("ambience", "brainstorm");
	public static final Holder<SoundEvent> AMBIENCE_GOLDEN_FOREST = registerForHolder("ambience", "golden_forest");
	public static final Holder<SoundEvent> AMBIENCE_LAKESIDE_DESSERT = registerForHolder("ambience", "lakeside_dessert");
	public static final Holder<SoundEvent> AMBIENCE_MYCOTIC_FOREST = registerForHolder("ambience", "mycotic_forest");
	public static final Holder<SoundEvent> AMBIENCE_PULSE_FOREST = registerForHolder("ambience", "pulse_forest");
	public static final Holder<SoundEvent> AMBIENCE_WIND_VALLEY = registerForHolder("ambience", "wind_valley");
	
	public static final SoundEvent DISKWING_AMBIENT = register("entity", "diskwing", "ambient");
	public static final SoundEvent DISKWING_DAMAGE = register("entity", "diskwing", "damage");
	
	private static SoundEvent register(String... path) {
		StringBuilder builder = new StringBuilder(EdenRing.MOD_ID);
		for (String part: path) {
			builder.append('.');
			builder.append(part);
		}
		String id = builder.toString();
		var key = EdenRing.makeID(id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, key, SoundEvent.createVariableRangeEvent(key));
	}

	private static Holder<SoundEvent> registerForHolder(String... path) {
		StringBuilder builder = new StringBuilder(EdenRing.MOD_ID);
		for (String part: path) {
			builder.append('.');
			builder.append(part);
		}
		String id = builder.toString();
		var key = EdenRing.makeID(id);
		return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, key, SoundEvent.createVariableRangeEvent(key));
	}
	
	public static void init() {}
}
