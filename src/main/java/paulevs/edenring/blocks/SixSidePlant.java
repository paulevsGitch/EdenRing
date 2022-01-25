package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.CustomColorProvider;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.ColorUtil;

import java.util.List;
import java.util.Map;

public class SixSidePlant extends BaseBlockNotFull implements CustomColorProvider, RenderLayerProvider {
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
	
	public boolean isWall(Level level, BlockPos pos, Direction face) {
		return level.getBlockState(pos).isFaceSturdy(level, pos, face.getOpposite());
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
		
		int index = face.getOpposite().get3DDataValue();
		BlockState state = level.getBlockState(pos);
		System.out.println(state);
		if (state.is(this)) {
			if (!state.getValue(DIRECTIONS[index]) && isWall(level, pos.relative(face.getOpposite()), face)) {
				return state.setValue(DIRECTIONS[index], true);
			}
			return null;
		}
		return defaultBlockState().setValue(DIRECTIONS[index], true);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
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
		return FabricToolTags.SHEARS.contains(tool.getItem());
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
		return (itemStack, i) -> i == 1 ? GrassColor.get(0.5D, 1.0D) : ColorUtil.color(255, 255, 255);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
