package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.LootParams;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenItems;

import java.util.List;

public class LimphiumBlock extends OverlayPlantBlock {
	public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
	
	public LimphiumBlock() {
		super(false);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(HALF);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Half half = state.getValue(HALF);
		if (half == Half.TOP) return world.getBlockState(pos.below()).is(this);
		BlockState below = world.getBlockState(pos.below());
		BlockState above = world.getBlockState(pos.above());
		return above.is(this) && (below.is(this) || isTerrain(below));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
	
	@Override
	//public List<ItemStack> getLoot(BlockState state, Builder builder) {
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		if (state.getValue(HALF) == Half.TOP) {
			return Lists.newArrayList(new ItemStack(EdenItems.LIMPHIUM_LEAF, MHelper.randRange(2, 4, MHelper.RANDOM_SOURCE)));
		}
		return MHelper.RANDOM.nextBoolean() ? Lists.newArrayList() : Lists.newArrayList(new ItemStack(EdenBlocks.LIMPHIUM_SAPLING));
	}
}
