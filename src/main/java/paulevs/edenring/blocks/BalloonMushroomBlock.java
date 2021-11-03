package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.items.BalloonMushroomBlockItem;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.interfaces.CustomItemProvider;

public class BalloonMushroomBlock extends BaseBlock implements CustomItemProvider {
	public BalloonMushroomBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).mapColor(MaterialColor.COLOR_PURPLE));
	}
	
	@Override
	public BlockItem getCustomItem(ResourceLocation resourceLocation, FabricItemSettings fabricItemSettings) {
		return new BalloonMushroomBlockItem(this, fabricItemSettings);
	}
}
