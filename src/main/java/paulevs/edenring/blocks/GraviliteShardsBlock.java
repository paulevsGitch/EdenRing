package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.betterx.bclib.interfaces.tools.AddMineableHammer;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;

public class GraviliteShardsBlock extends BaseAttachedBlock implements BlockModelProvider, RenderLayerProvider,
		AddMineableHammer, AddMineablePickaxe {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	
	public GraviliteShardsBlock() {
		super(FabricBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER).luminance(15).noCollision().noOcclusion());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return BOUNDING_SHAPES.get(state.getValue(FACING));
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ModelResourceLocation shardsUp = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), this.defaultBlockState().toString());
		
		if (!modelCache.containsKey(shardsUp)) {
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%modid%", stateId.getNamespace());
			textures.put("%texture%", stateId.getPath());
			Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
			BlockModel model = ModelsHelper.fromPattern(pattern);
			modelCache.put(shardsUp, model);
		}
		
		Direction facing = blockState.getValue(FACING);
		if (facing == Direction.UP) {
			return modelCache.get(shardsUp);
		}
		
		Transformation transformation = new Transformation(null, facing.getRotation(), null, null);
		return ModelsHelper.createMultiVariant(shardsUp, transformation, false);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		return ModelsHelper.createBlockItem(itemID);
	}
	
	static {
		BOUNDING_SHAPES.put(Direction.UP, box(2, 0, 2, 14, 15, 14));
		BOUNDING_SHAPES.put(Direction.DOWN, box(2, 1, 2, 14, 16, 14));
		BOUNDING_SHAPES.put(Direction.NORTH, box(2, 2, 1, 14, 14, 16));
		BOUNDING_SHAPES.put(Direction.SOUTH, box(2, 2, 0, 14, 14, 15));
		BOUNDING_SHAPES.put(Direction.WEST, box(1, 2, 2, 16, 14, 14));
		BOUNDING_SHAPES.put(Direction.EAST, box(0, 2, 2, 15, 14, 14));
	}
}
