package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseRotatedPillarBlock;

public class BrainTreeLogBlock extends BaseRotatedPillarBlock {
	public BrainTreeLogBlock() {
		super(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
	}
}
