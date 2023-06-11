package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.interfaces.tools.AddMineableHoe;
import org.betterx.bclib.items.tool.BaseShearsItem;
import org.betterx.bclib.util.BlocksHelper;
import paulevs.edenring.blocks.EdenBlockProperties.QuadShape;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShadedVineBlock extends BaseBlockNotFull implements RenderLayerProvider, BonemealableBlock,
		AddMineableHoe {
	public static final EnumProperty<QuadShape> SHAPE = EdenBlockProperties.QUAD_SHAPE;
	private static final VoxelShape VOXEL_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
	
	public ShadedVineBlock() {
		this(BehaviourBuilders.createPlant().sound(SoundType.VINE).noCollission().noOcclusion());
	}
	
	public ShadedVineBlock(Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(SHAPE, QuadShape.SMALL));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String modId = stateId.getNamespace();
		String name = stateId.getPath();
		
		QuadShape shape = blockState.getValue(SHAPE);
		switch (shape) {
			case TOP -> name += "_top";
			case BOTTOM -> name += "_bottom";
			case SMALL -> name += "_small";
		}
		
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%modid%", modId);
		textures.put("%texture%", name);
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%modid%", modId);
		textures.put("%texture%", name + "_bottom");
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.ITEM_BLOCK, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return VOXEL_SHAPE;
	}
	
	public boolean canGenerate(BlockState state, LevelReader world, BlockPos pos) {
		return isSupport(state, world, pos);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return isSupport(state, world, pos);
	}
	
	protected boolean isSupport(BlockState state, LevelReader world, BlockPos pos) {
		BlockState up = world.getBlockState(pos.above());
		return up.is(this) || up.is(BlockTags.LEAVES) || canSupportCenter(world, pos.above(), Direction.DOWN);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		else {
			if (!world.getBlockState(pos.below()).is(this)) {
				if (world.getBlockState(pos.above()).is(this)) return state.setValue(SHAPE, QuadShape.BOTTOM);
				return state.setValue(SHAPE, QuadShape.SMALL);
			}
			else if (!world.getBlockState(pos.above()).is(this)) return state.setValue(SHAPE, QuadShape.TOP);
			return state.setValue(SHAPE, QuadShape.MIDDLE);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && BaseShearsItem.isShear(tool) || EnchantmentHelper.getItemEnchantmentLevel(
			Enchantments.SILK_TOUCH,
			tool
		) > 0) {
			return Lists.newArrayList(new ItemStack(this));
		}
		else {
			return Lists.newArrayList();
		}
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state, boolean isClient) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		return world.getBlockState(pos).isAir();
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		return world.isEmptyBlock(pos);
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		while (world.getBlockState(pos).getBlock() == this) {
			pos = pos.below();
		}
		world.setBlockAndUpdate(pos, defaultBlockState());
		BlocksHelper.setWithoutUpdate(world, pos, defaultBlockState());
	}
}
