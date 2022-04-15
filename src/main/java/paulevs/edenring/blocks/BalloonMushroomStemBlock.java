package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.EdenBlockProperties.BalloonMushroomStemState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;

import java.util.Map;

public class BalloonMushroomStemBlock extends BaseBlockNotFull implements RenderLayerProvider {
	public static final EnumProperty<BalloonMushroomStemState> BALLOON_MUSHROOM_STEM = EdenBlockProperties.BALLOON_MUSHROOM_STEM;
	private static final Map<BalloonMushroomStemState, ResourceLocation> MODELS = Maps.newEnumMap(BalloonMushroomStemState.class);
	private static final Map<BalloonMushroomStemState, VoxelShape> SHAPES = Maps.newEnumMap(BalloonMushroomStemState.class);
	
	public BalloonMushroomStemBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).noOcclusion().isSuffocating(EdenBlocks::never).isViewBlocking(EdenBlocks::never));
		registerDefaultState(stateDefinition.any().setValue(BALLOON_MUSHROOM_STEM, BalloonMushroomStemState.UP));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(BALLOON_MUSHROOM_STEM);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = this.defaultBlockState();
		
		Axis axis = ctx.getClickedFace().getAxis();
		switch (axis) {
			case X: return blockState.setValue(BALLOON_MUSHROOM_STEM, BalloonMushroomStemState.EAST_WEST);
			case Z: return blockState.setValue(BALLOON_MUSHROOM_STEM, BalloonMushroomStemState.NORTH_SOUTH);
		}
		
		return blockState;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		VoxelShape shape = SHAPES.get(state.getValue(BALLOON_MUSHROOM_STEM));
		return shape == null ? Shapes.block() : shape;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		BalloonMushroomStemState shape = blockState.getValue(BALLOON_MUSHROOM_STEM);
		if (shape == BalloonMushroomStemState.UP || shape == BalloonMushroomStemState.NORTH_SOUTH || shape == BalloonMushroomStemState.EAST_WEST) {
			return SHAPES.get(shape);
		}
		return Shapes.empty();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		BalloonMushroomStemState state = blockState.getValue(BALLOON_MUSHROOM_STEM);
		ResourceLocation modelId = MODELS.get(state);
		if (modelId != null) {
			if (state == BalloonMushroomStemState.NORTH_SOUTH) {
				return ModelsHelper.createRotatedModel(modelId, Axis.Z);
			}
			if (state == BalloonMushroomStemState.EAST_WEST) {
				return ModelsHelper.createRotatedModel(modelId, Axis.X);
			}
			return ModelsHelper.createBlockSimple(modelId);
		}
		return super.getModelVariant(stateId, blockState, modelCache);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BalloonMushroomStemState stem = state.getValue(BALLOON_MUSHROOM_STEM);
		if (stem == BalloonMushroomStemState.FUR) {
			BlockPos sidePos = pos.above();
			return world.getBlockState(sidePos).isFaceSturdy(world, sidePos, Direction.DOWN);
		}
		if (stem == BalloonMushroomStemState.THIN || stem == BalloonMushroomStemState.THIN_TOP) {
			BlockPos below = pos.below();
			BlockState belowState = world.getBlockState(below);
			if (belowState.is(this) || belowState.getBlock() instanceof MycoticLanternBlock || belowState.isFaceSturdy(world, below, Direction.UP)) {
				below = pos.above();
				belowState = world.getBlockState(below);
				if (belowState.is(this) || belowState.isFaceSturdy(world, below, Direction.DOWN)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			if (world.getBlockState(pos.above()).is(EdenBlocks.BALLOON_MUSHROOM_BLOCK) && !world.getBlockState(pos.above(2)).is(EdenBlocks.BALLOON_MUSHROOM_BLOCK)) {
				world.removeBlock(pos.above(), true);
			}
			return Blocks.AIR.defaultBlockState();
		}
		return state;
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return blockState.getFluidState().isEmpty();
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	static {
		SHAPES.put(BalloonMushroomStemState.UP, Block.box(4, 0, 4, 12, 16, 12));
		SHAPES.put(BalloonMushroomStemState.NORTH_SOUTH, Block.box(4, 4, 0, 12, 12, 16));
		SHAPES.put(BalloonMushroomStemState.EAST_WEST, Block.box(0, 4, 4, 16, 12, 12));
		SHAPES.put(BalloonMushroomStemState.THIN, Block.box(7, 0, 7, 9, 16, 9));
		SHAPES.put(BalloonMushroomStemState.THIN_TOP, Block.box(2, 0, 2, 14, 16, 14));
		SHAPES.put(BalloonMushroomStemState.FUR, Block.box(0, 7, 0, 16, 16, 16));
		
		MODELS.put(BalloonMushroomStemState.UP, EdenRing.makeID("block/balloon_mushroom_stem"));
		MODELS.put(BalloonMushroomStemState.NORTH_SOUTH, EdenRing.makeID("block/balloon_mushroom_stem"));
		MODELS.put(BalloonMushroomStemState.EAST_WEST, EdenRing.makeID("block/balloon_mushroom_stem"));
		MODELS.put(BalloonMushroomStemState.THIN, EdenRing.makeID("block/balloon_mushroom_stem_thin"));
		MODELS.put(BalloonMushroomStemState.THIN_TOP, EdenRing.makeID("block/balloon_mushroom_stem_top"));
		MODELS.put(BalloonMushroomStemState.FUR, EdenRing.makeID("block/balloon_mushroom_fur"));
	}
}
