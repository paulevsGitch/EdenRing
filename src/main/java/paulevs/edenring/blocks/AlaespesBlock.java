package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import paulevs.edenring.EdenRing;

import java.util.Map;
import java.util.Optional;

public class AlaespesBlock extends OverlayDoublePlantBlock {
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Optional<String> pattern;
		if (blockState.getValue(TOP)) {
			pattern = PatternsHelper.createJson(BasePatterns.BLOCK_EMPTY, EdenRing.makeID("alaespes_color"));
		}
		else {
			pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/alaespes.json"), Maps.newHashMap());
		}
		return ModelsHelper.fromPattern(pattern);
	}
}
