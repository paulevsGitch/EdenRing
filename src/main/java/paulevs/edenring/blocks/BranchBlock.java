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
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.EdenBlockProperties.BalloonMushroomStemState;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.util.BlocksHelper;

import java.util.Map;
import java.util.Optional;

public class BranchBlock extends BaseBlockNotFull {
	public static final BooleanProperty[] DIRECTIONS = EdenBlockProperties.DIRECTIONS;
	private static final VoxelShape CENTER = box(4, 4, 4, 12, 12, 12);
	private static final VoxelShape[] DIRECTION_SHAPES = new VoxelShape[] {
		box(4, 0, 4, 12, 4, 12), // DOWN
		box(4, 12, 4, 12, 16, 12), // UP
		box(4, 4, 0, 12, 12, 4), // NORTH
		box(4, 4, 12, 12, 12, 16), // SOUTH
		box(0, 4, 4, 4, 12, 12), // WEST
		box(12, 4, 4, 16, 12, 12) // EAST
	};
	private final Map<BlockState, VoxelShape> shapesCache = Maps.newHashMap();
	
	public BranchBlock(Block block) {
		this(FabricBlockSettings.copyOf(block));
	}
	
	public BranchBlock(Properties properties) {
		super(properties);
		BlockState state = getStateDefinition().any();
		for (BooleanProperty property: DIRECTIONS) {
			state = state.setValue(property, false);
		}
		registerDefaultState(state);
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(DIRECTIONS);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return shapesCache.computeIfAbsent(blockState, i -> {
			VoxelShape voxelShape = CENTER;
			for (byte index = 0; index < DIRECTIONS.length; index++) {
				if (blockState.getValue(DIRECTIONS[index])) {
					voxelShape = Shapes.or(voxelShape, DIRECTION_SHAPES[index]);
				}
			}
			return voxelShape;
		});
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		return getConnectedState(level, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		return getConnectedState(level, pos);
	}
	
	public BlockState getConnectedState(LevelAccessor level, BlockPos pos) {
		BlockState state = defaultBlockState();
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (isWall(level, pos.relative(dir), dir)) {
				int index = dir.get3DDataValue();
				state = state.setValue(DIRECTIONS[index], true);
			}
		}
		return state;
	}
	
	private boolean isWall(LevelAccessor level, BlockPos pos, Direction face) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof BalloonMushroomStemBlock) {
			BalloonMushroomStemState stem = state.getValue(BalloonMushroomStemBlock.BALLOON_MUSHROOM_STEM);
			if (stem == BalloonMushroomStemState.UP && face.getAxis() == Axis.Y) return true;
			if (stem == BalloonMushroomStemState.EAST_WEST && face.getAxis() == Axis.X) return true;
			if (stem == BalloonMushroomStemState.NORTH_SOUTH && face.getAxis() == Axis.Z) return true;
		}
		return state.is(this) || state.isFaceSturdy(level, pos, face.getOpposite());
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		BlockState def = defaultBlockState();
		String modID = stateId.getNamespace();
		String name = stateId.getPath();
		
		ModelResourceLocation keyCenter = new ModelResourceLocation(modID, name, def.toString());
		ModelResourceLocation keyUp = new ModelResourceLocation(modID, name, def.setValue(BlockStateProperties.UP, true).toString());
		
		if (!modelCache.containsKey(keyCenter)) {
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%texture%", modID + ":block/" + name);
			Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/branch_center.json"), textures);
			BlockModel model = ModelsHelper.fromPattern(pattern);
			modelCache.put(keyCenter, model);
			
			pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/branch_side.json"), textures);
			model = ModelsHelper.fromPattern(pattern);
			modelCache.put(keyUp, model);
		}
		
		MultiPartBuilder builder = MultiPartBuilder.create(stateDefinition);
		builder.part(keyCenter).add();
		
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			Transformation transformation = new Transformation(null, dir.getRotation(), null, null);
			int index = dir.get3DDataValue();
			final BooleanProperty prop = DIRECTIONS[index];
			builder.part(keyUp).setTransformation(transformation).setCondition(state -> state.getValue(prop)).add();
		}
		
		return builder.build();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", itemID.getNamespace() + ":block/" + itemID.getPath());
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/block/branch_center.json"), textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
