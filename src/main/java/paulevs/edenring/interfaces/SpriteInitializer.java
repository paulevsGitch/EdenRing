package paulevs.edenring.interfaces;

import net.minecraft.core.BlockPos;
import paulevs.edenring.client.environment.animation.SpriteAnimation;

import java.util.Random;

@FunctionalInterface
public interface SpriteInitializer {
	SpriteAnimation init(BlockPos origin, Random random);
}
