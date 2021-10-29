package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseBlock;

public class BalloonMushroomBlock extends BaseBlock {
	public BalloonMushroomBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM));
	}
}
