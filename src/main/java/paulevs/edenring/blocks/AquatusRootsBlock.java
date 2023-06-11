package paulevs.edenring.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AquatusRootsBlock extends BaseBlockNotFull implements RenderLayerProvider {
	private static final VoxelShape TOP_SHAPE = box(1, 8, 1, 15, 16, 15);
	public static final BooleanProperty UP = BlockStateProperties.UP;
	
	public AquatusRootsBlock() {
		super(FabricBlockSettings.copyOf(Blocks.DANDELION).randomTicks());
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(UP);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		BlockPos above = pos.above();
		if (state.getValue(UP)) {
			if (!world.getBlockState(above).isFaceSturdy(world, above, Direction.DOWN)) {
				return Blocks.AIR.defaultBlockState();
			}
			return state;
		}
		BlockPos below = pos.below();
		if (!world.getBlockState(above).isFaceSturdy(world, above, Direction.DOWN) || !world.getBlockState(below).isFaceSturdy(world, below, Direction.UP)) {
			return Blocks.AIR.defaultBlockState();
		}
		return state;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createBlockItem(resourceLocation);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation texture = blockState.getValue(UP) ? EdenRing.makeID("aquatus_outer_leaves") : stateId;
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, texture);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		if (state.getValue(UP)) {
			return;
		}
		int px = pos.getX() + MHelper.randRange(-4, 4, randomSource);
		int pz = pos.getZ() + MHelper.randRange(-4, 4, randomSource);
		BlockPos repPos = new BlockPos(px, pos.getY() - 1, pz);
		if (canReplace(level.getBlockState(repPos))) {
			level.setBlockAndUpdate(repPos, randomSource.nextInt(4) == 0 ? Blocks.GRAVEL.defaultBlockState() : Blocks.SAND.defaultBlockState());
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return state.getValue(UP) && MHelper.RANDOM.nextInt(4) == 0 ? Collections.singletonList(new ItemStack(EdenBlocks.AQUATUS_SAPLING)) : Collections.EMPTY_LIST;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return state.getValue(UP) ? TOP_SHAPE : super.getShape(state, view, pos, ePos);
	}
	
	private boolean canReplace(BlockState state) {
		return state.is(Blocks.DIRT) || state.getBlock() instanceof GrassBlock;
	}
}
