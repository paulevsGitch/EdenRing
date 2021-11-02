package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class AuritisOuterLeaves extends BaseBlockNotFull implements BonemealableBlock, RenderLayerProvider {
	public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
	private static final VoxelShape VOXEL_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
	
	public AuritisOuterLeaves() {
		super(FabricBlockSettings.of(Material.PLANT).mapColor(MaterialColor.GOLD).breakByTool(FabricToolTags.SHEARS).breakByHand(true).sound(SoundType.GRASS).noCollission());
		registerDefaultState(getStateDefinition().any().setValue(BOTTOM, true));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(BOTTOM);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.above();
		BlockState above = world.getBlockState(blockPos);
		return above.is(this) || above.is(BlockTags.LEAVES) || canSupportCenter(world, blockPos, Direction.DOWN);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return !this.canSurvive(state, world, pos) ? Blocks.AIR.defaultBlockState() : state.setValue(BOTTOM, !world.getBlockState(pos.below()).is(this));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return VOXEL_SHAPE;
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level level, Random random, BlockPos blockPos, BlockState blockState) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState blockState) {
		ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this));
		level.addFreshEntity(item);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%modid%", itemID.getNamespace());
		textures.put("%texture%", itemID.getPath() + "_bottom");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.ITEM_BLOCK, textures);
		return  ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Map<String, String> textures = Maps.newHashMap();
		String name = blockState.getValue(BOTTOM) ? stateId.getPath() + "_bottom" : stateId.getPath();
		textures.put("%modid%", stateId.getNamespace());
		textures.put("%texture%", name);
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
