package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.interfaces.tools.AddMineableHammer;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;

public class GraviliteLampBlock extends BaseBlock implements AddMineableHammer, AddMineablePickaxe {
	public GraviliteLampBlock() {
		super(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(15));
	}
}
