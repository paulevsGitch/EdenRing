package paulevs.edenring.client.environment.animation;

import net.minecraft.core.BlockPos;

import java.util.Random;

@FunctionalInterface
public interface SpriteInitializer {
	SpriteAnimation init(BlockPos origin, Random random);
}
