package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import org.betterx.bclib.blocks.BaseRotatedPillarBlock;

public class AquatusBlock extends BaseRotatedPillarBlock {
	public AquatusBlock() {
		super(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).sounds(SoundType.WART_BLOCK));
	}
}
