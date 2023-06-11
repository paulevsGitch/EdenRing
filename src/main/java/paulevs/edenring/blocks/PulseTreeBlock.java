package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.EdenBlockProperties.PulseTreeState;
import paulevs.edenring.registries.EdenBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PulseTreeBlock extends BaseBlockNotFull implements RenderLayerProvider {
	public static final EnumProperty<PulseTreeState> PULSE_TREE = EdenBlockProperties.PULSE_TREE;
	private static final Map<PulseTreeState, VoxelShape> SHAPES = Maps.newEnumMap(PulseTreeState.class);
	
	public PulseTreeBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).mapColor(MapColor.COLOR_CYAN).noOcclusion().isSuffocating(EdenBlocks::never).isViewBlocking(EdenBlocks::never));
		registerDefaultState(stateDefinition.any().setValue(PULSE_TREE, PulseTreeState.UP));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(PULSE_TREE);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState blockState = this.defaultBlockState();
		
		Axis axis = ctx.getClickedFace().getAxis();
		switch (axis) {
			case X: return blockState.setValue(PULSE_TREE, PulseTreeState.EAST_WEST);
			case Z: return blockState.setValue(PULSE_TREE, PulseTreeState.NORTH_SOUTH);
		}
		
		return blockState;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		VoxelShape shape = SHAPES.get(state.getValue(PULSE_TREE));
		return shape == null ? Shapes.block() : shape;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		PulseTreeState state = blockState.getValue(PULSE_TREE);
		
		if (state == PulseTreeState.NORTH_SOUTH) {
			return ModelsHelper.createRotatedModel(EdenRing.makeID("block/pulse_tree_up"), Axis.Z);
		}
		if (state == PulseTreeState.EAST_WEST) {
			return ModelsHelper.createRotatedModel(EdenRing.makeID("block/pulse_tree_up"), Axis.X);
		}
		
		ResourceLocation modelId = EdenRing.makeID("block/pulse_tree_" + state.getSerializedName());
		
		return ModelsHelper.createBlockSimple(modelId);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/pulse_tree_up.json"), Maps.newHashMap());
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		PulseTreeState stem = state.getValue(PULSE_TREE);
		if (stem == PulseTreeState.NORTH_SOUTH || stem == PulseTreeState.EAST_WEST) return true;
		BlockState below = world.getBlockState(pos.below());
		if (stem == PulseTreeState.UP) {
			boolean canStay = true;
			for (byte i = 1; i < 4; i++) {
				BlockState above = world.getBlockState(pos.above(i));
				if (above.is(this) && above.getValue(PULSE_TREE).isNatural()) {
					canStay = false;
					break;
				}
			}
			if (canStay) return true;
		}
		return below.is(this) || below.isFaceSturdy(world, pos.below(), Direction.UP);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		world.scheduleTick(pos, this, 1);
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
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
		if (!canSurvive(blockState, serverLevel, blockPos)) {
			serverLevel.destroyBlock(blockPos, true);
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drop = new ArrayList<>();
		drop.add(new ItemStack(this));
		PulseTreeState stem = state.getValue(PULSE_TREE);
		if (stem != PulseTreeState.UP && stem != PulseTreeState.NORTH_SOUTH && stem != PulseTreeState.EAST_WEST && MHelper.RANDOM.nextInt(4) == 0) {
			drop.add(new ItemStack(EdenBlocks.PULSE_TREE_SAPLING));
		}
		else if (stem == PulseTreeState.HEAD_SMALL) {
			drop.add(new ItemStack(EdenBlocks.PULSE_TREE_SAPLING));
		}
		return drop;
	}
	
	static {
		SHAPES.put(PulseTreeState.UP, Block.box(6, 0, 6, 10, 16, 10));
		SHAPES.put(PulseTreeState.NORTH_SOUTH, Block.box(6, 6, 0, 10, 10, 16));
		SHAPES.put(PulseTreeState.EAST_WEST, Block.box(0, 6, 6, 16, 10, 10));
		
		VoxelShape stem = Block.box(6, 0, 6, 10, 12, 10);
		SHAPES.put(PulseTreeState.HEAD_BIG, Shapes.or(stem, Block.box(0, 12, 0, 16, 16, 16)));
		SHAPES.put(PulseTreeState.HEAD_MEDIUM, Shapes.or(stem, Block.box(2, 12, 2, 14, 16, 14)));
		SHAPES.put(PulseTreeState.HEAD_SMALL, Shapes.or(stem, Block.box(4, 12, 4, 12, 16, 12)));
	}
}
