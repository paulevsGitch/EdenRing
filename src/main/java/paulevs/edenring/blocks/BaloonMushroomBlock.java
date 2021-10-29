package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseBlock;

public class BaloonMushroomBlock extends BaseBlock {
	public BaloonMushroomBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM));
	}
}
