package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;

public class MossyStoneBlock extends BaseBlock {
	public MossyStoneBlock() {
		super(FabricBlockSettings.copyOf(Blocks.STONE));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String modId = blockId.getNamespace();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", modId + ":block/mossy_stone_top");
		textures.put("%side%", modId + ":block/mossy_stone_side");
		textures.put("%bottom%", "minecraft:block/stone");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
