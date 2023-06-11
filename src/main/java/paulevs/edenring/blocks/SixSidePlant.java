package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.items.tool.BaseShearsItem;
import org.betterx.bclib.util.BlocksHelper;
import org.betterx.ui.ColorUtil;

import java.util.List;
import java.util.Map;

public class SixSidePlant extends BaseBlockNotFull implements CustomColorProvider, RenderLayerProvider, BonemealableBlock {
	public static final BooleanProperty[] DIRECTIONS = EdenBlockProperties.DIRECTIONS;
	private static final VoxelShape UP_AABB = box(0, 15, 0, 16, 16, 16);
	private static final VoxelShape DOWN_AABB = box(0, 0, 0, 16, 1, 16);
	private static final VoxelShape WEST_AABB = box(0, 0, 0, 1, 16, 16);
	private static final VoxelShape EAST_AABB = box(15, 0, 0, 16, 16, 16);
	private static final VoxelShape NORTH_AABB = box(0, 0, 0, 16, 16, 1);
	private static final VoxelShape SOUTH_AABB = box(0, 0, 15, 16, 16, 16);
	
	private final Map<BlockState, VoxelShape> shapesCache = Maps.newHashMap();
	
	public SixSidePlant(FabricBlockSettings settings) {
		super(settings);
		BlockState state = getStateDefinition().any();
		for (BooleanProperty property: DIRECTIONS) {
			state = state.setValue(property, false);
		}
		registerDefaultState(state);
	}
	
	public boolean isWall(LevelAccessor level, BlockPos pos, Direction face) {
		BlockState state = level.getBlockState(pos);
		return state.isFaceSturdy(level, pos, face.getOpposite()) || state.is(BlockTags.LEAVES);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(DIRECTIONS);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction face = ctx.getClickedFace();
		BlockPos pos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		
		Direction opposite = face.getOpposite();
		int index = opposite.get3DDataValue();
		BlockState state = level.getBlockState(pos);
		if (state.is(this)) {
			if (!state.getValue(DIRECTIONS[index]) && isWall(level, pos.relative(opposite), opposite)) {
				return state.setValue(DIRECTIONS[index], true);
			}
			return null;
		}
		if (isWall(level, pos.relative(opposite), opposite)) {
			return defaultBlockState().setValue(DIRECTIONS[index], true);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && (isShears(tool) || hasSilkTouch(tool))) {
			byte count = getCount(state);
			return count > 0 ? Lists.newArrayList(new ItemStack(this, count)) : Lists.newArrayList();
		}
		else {
			return Lists.newArrayList();
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return shapesCache.computeIfAbsent(blockState, i -> {
			VoxelShape voxelShape = Shapes.empty();
			if (blockState.getValue(BlockStateProperties.UP).booleanValue()) {
				voxelShape = UP_AABB;
			}
			if (blockState.getValue(BlockStateProperties.DOWN).booleanValue()) {
				voxelShape = DOWN_AABB;
			}
			if (blockState.getValue(BlockStateProperties.NORTH).booleanValue()) {
				voxelShape = Shapes.or(voxelShape, NORTH_AABB);
			}
			if (blockState.getValue(BlockStateProperties.SOUTH).booleanValue()) {
				voxelShape = Shapes.or(voxelShape, SOUTH_AABB);
			}
			if (blockState.getValue(BlockStateProperties.EAST).booleanValue()) {
				voxelShape = Shapes.or(voxelShape, EAST_AABB);
			}
			if (blockState.getValue(BlockStateProperties.WEST).booleanValue()) {
				voxelShape = Shapes.or(voxelShape, WEST_AABB);
			}
			return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
		});
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
		BlockState blockState2 = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());
		return blockState2.is(this);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		byte count = 0;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			int index = dir.get3DDataValue();
			if (state.getValue(DIRECTIONS[index])) {
				if (!isWall(world, pos.relative(dir), dir)) {
					state = state.setValue(DIRECTIONS[index], false);
					count++;
				}
			}
			else {
				count++;
			}
		}
		return count == 6 ? Blocks.AIR.defaultBlockState() : state;
	}
	
	private byte getCount(BlockState state) {
		byte result = 0;
		for (BooleanProperty property: DIRECTIONS) {
			if (state.getValue(property)) {
				result++;
			}
		}
		return result;
	}
	
	private boolean isShears(ItemStack tool) {
		return BaseShearsItem.isShear(tool);
	}
	
	private boolean hasSilkTouch(ItemStack tool) {
		return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockColor getProvider() {
		return (blockState, blockAndTintGetter, blockPos, i) -> blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos) : GrassColor.get(0.5D, 1.0D);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemColor getItemProvider() {
		return (itemStack, i) -> i == 0 ? GrassColor.get(0.5D, 1.0D) : ColorUtil.color(255, 255, 255);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	public BlockState getAttachedState(LevelAccessor level, BlockPos pos) {
		BlockState state = defaultBlockState();
		boolean isEmpty = true;
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (isWall(level, pos.relative(dir), dir)) {
				int index = dir.get3DDataValue();
				state = state.setValue(DIRECTIONS[index], true);
				isEmpty = false;
			}
		}
		return isEmpty ? null : state;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader blockGetter, BlockPos blockPos, BlockState blockState, boolean bl) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos blockPos, BlockState blockState) {
		return true;
	}
	
	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this));
		level.addFreshEntity(item);
	}
}
