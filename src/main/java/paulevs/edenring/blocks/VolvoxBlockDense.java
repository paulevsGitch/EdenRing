package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;

public class VolvoxBlockDense extends BaseBlock {
	public VolvoxBlockDense() {
		super(BehaviourBuilders.createWood().strength(1F).sound(SoundType.SLIME_BLOCK));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Optional<String> pattern = PatternsHelper.createBlockSimple(itemID);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation model1 = new ResourceLocation(stateId.getNamespace(), "volvox_block_dense");
		ResourceLocation model2 = new ResourceLocation(stateId.getNamespace(), "volvox_block_dense_mossy");
		modelCache.put(model1, ModelsHelper.fromPattern(PatternsHelper.createBlockSimple(model1)));
		modelCache.put(model2, ModelsHelper.fromPattern(PatternsHelper.createBlockSimple(model2)));
		
		return new MultiVariant(Lists.newArrayList(
			new Variant(model1, Transformation.identity(), false, 2),
			new Variant(model2, Transformation.identity(), false, 1)
		));
	}
}
