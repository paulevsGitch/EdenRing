package paulevs.edenring.paintings;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

public interface PaintingColorProvider<L extends ClientLevel, B extends BlockPos> {
	int getColor(L level, B pos);
}
