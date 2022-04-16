package paulevs.edenring;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.blocks.OverlayPlantBlock;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;

import java.util.Optional;

public class LimphiumBlock extends OverlayPlantBlock {
	public LimphiumBlock() {
		super(false);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/" + blockId.getPath() + ".json"), Maps.newHashMap());
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/" + itemID.getPath() + ".json"), Maps.newHashMap());
		return ModelsHelper.fromPattern(pattern);
	}
}
