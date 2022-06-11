package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;

import java.util.Map;
import java.util.Optional;

public class MycoticLanternBlock extends BaseBlockNotFull {
	private static final VoxelShape SHAPE = Shapes.or(box(2, 2, 2, 14, 14, 14), box(5, 0, 5, 11, 16, 11));
	
	public MycoticLanternBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).luminance(15));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Map<String, String> textures = Maps.newHashMap();
		String texture = stateId.getNamespace() + ":block/" + stateId.getPath();
		textures.put("%side%", texture + "_side");
		textures.put("%top%", texture + "_top");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_MYCOTIC_LANTERN, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Map<String, String> textures = Maps.newHashMap();
		String texture = itemID.getNamespace() + ":block/" + itemID.getPath();
		textures.put("%side%", texture + "_side");
		textures.put("%top%", texture + "_top");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_MYCOTIC_LANTERN, textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
