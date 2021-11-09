package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import ru.bclib.blocks.BaseBlock;

public class GraviliteBlock extends BaseBlock {
	public GraviliteBlock() {
		super(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(15));
	}
}
