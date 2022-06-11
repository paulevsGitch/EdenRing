package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import org.betterx.bclib.blocks.BaseBlock;

public class GraviliteLampBlock extends BaseBlock {
	public GraviliteLampBlock() {
		super(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(15));
	}
}
