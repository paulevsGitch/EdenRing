package paulevs.edenring.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import paulevs.edenring.client.environment.animation.SpriteAnimation;

@FunctionalInterface
public interface SpriteInitializer {
	SpriteAnimation init(BlockPos origin, RandomSource random);
}
