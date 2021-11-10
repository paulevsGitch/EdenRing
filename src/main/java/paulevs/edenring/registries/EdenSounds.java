package paulevs.edenring.registries;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import paulevs.edenring.EdenRing;

public class EdenSounds {
	public static final SoundEvent BLOCK_ELECTRIC = register("block", "electric");
	
	private static SoundEvent register(String type, String id) {
		return Registry.register(Registry.SOUND_EVENT, EdenRing.makeID(type + "." + id), new SoundEvent(EdenRing.makeID(id)));
	}
	
	public static void init() {}
}
