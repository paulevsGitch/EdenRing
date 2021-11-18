package paulevs.edenring.world.biomes;

import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import paulevs.edenring.EdenRing;
import ru.bclib.config.IdConfig;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.biomes.BCLBiomeDef;

public class ErodedCaveBiome extends BCLBiome {
	public ErodedCaveBiome(IdConfig config, ConfiguredSurfaceBuilder surfaceBuilder) {
		super(new BCLBiomeDef(EdenRing.makeID("eroded_cave"))
			.loadConfigValues(config)
			.setSurface(surfaceBuilder)
			.setPlantsColor(255, 174, 100)
			.setSkyColor(113, 178, 255)
			.setFogColor(183, 212, 255)
		);
	}
}
