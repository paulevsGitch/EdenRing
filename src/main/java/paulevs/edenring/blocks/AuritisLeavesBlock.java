package paulevs.edenring.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import paulevs.edenring.EdenRing;
import ru.bclib.blocks.BaseLeavesBlock;
import ru.bclib.client.models.ModelsHelper;

import java.util.Map;

public class AuritisLeavesBlock extends BaseLeavesBlock {
	public AuritisLeavesBlock() {
		super(Blocks.OAK_SAPLING, MaterialColor.GOLD);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		return ModelsHelper.createBlockSimple(EdenRing.makeID("block/auritis_leaves"));
	}
}
