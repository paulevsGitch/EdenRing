package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.blocks.BaseRotatedPillarBlock;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;
import org.betterx.bclib.interfaces.tools.AddMineableHammer;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;

public class GraviliteBlock extends BaseRotatedPillarBlock implements AddMineableHammer, AddMineablePickaxe {
	public GraviliteBlock() {
		super(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK).luminance(15));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ModelResourceLocation pillarUp = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), this.defaultBlockState().toString());
		
		if (!modelCache.containsKey(pillarUp)) {
			Map<String, String> textures = Maps.newHashMap();
			String modId = stateId.getNamespace();
			String name = stateId.getPath();
			textures.put("%side%", modId + ":block/" + name + "_side");
			textures.put("%end%", modId + ":block/" + name + "_top");
			Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_PILLAR_NO_SHADE, textures);
			modelCache.put(pillarUp, ModelsHelper.fromPattern(pattern));
		}
		
		return ModelsHelper.createRotatedModel(pillarUp, blockState.getValue(AXIS));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Map<String, String> textures = Maps.newHashMap();
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		textures.put("%side%", modId + ":block/" + name + "_side");
		textures.put("%end%", modId + ":block/" + name + "_top");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_PILLAR_NO_SHADE, textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
