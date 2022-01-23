package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.EdenRing;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;

import java.util.Map;
import java.util.Optional;

public class SymbioticMoldBlock extends CeilPlantBlock {
	public SymbioticMoldBlock(int emission) {
		super(FabricBlockSettings.copyOf(Blocks.WARPED_ROOTS).luminance(emission));
	}
	
	@Override
	@Nullable
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String modId = blockId.getNamespace();
		String name = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", modId + ":block/" + name);
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/translucent_plant.json"), textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}
}
