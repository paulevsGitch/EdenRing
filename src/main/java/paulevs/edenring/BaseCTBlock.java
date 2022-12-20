package paulevs.edenring;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.ModelsHelper.MultiPartBuilder;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.util.BlocksHelper;
import org.joml.Vector3f;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.blocks.EdenPatterns;

import java.util.Map;
import java.util.Optional;

public class BaseCTBlock extends BaseBlock implements RenderLayerProvider {
	public static final BooleanProperty[] DIRECTIONS = EdenBlockProperties.DIRECTIONS;
	
	public BaseCTBlock(Properties settings) {
		super(settings);
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
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		return getConnectedState(state, level, pos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		return getConnectedState(null, level, pos);
	}
	
	public BlockState getConnectedState(BlockState state, LevelAccessor level, BlockPos pos) {
		if (state == null) state = defaultBlockState();
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			boolean value = canConnect(state, dir, level.getBlockState(pos.relative(dir)));
			state = state.setValue(DIRECTIONS[dir.get3DDataValue()], value);
		}
		return state;
	}
	
	protected boolean canConnect(BlockState center, Direction dir, BlockState side) {
		return side.is(this);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		BlockState def = defaultBlockState();
		String modID = stateId.getNamespace();
		String name = stateId.getPath();
		
		ModelResourceLocation keyCube = new ModelResourceLocation(modID, name, def.toString());
		ModelResourceLocation[] keyQuad = new ModelResourceLocation[4];
		for (byte i = 0; i < 4; i++) {
			keyQuad[i] = new ModelResourceLocation(modID, name, def + "_" + i);
		}
		
		if (!modelCache.containsKey(keyCube)) {
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%modid%", modID);
			textures.put("%texture%", name);
			Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_BASE, textures);
			
			BlockModel model = ModelsHelper.fromPattern(pattern);
			modelCache.put(keyCube, model);
			
			for (byte i = 0; i < 4; i++) {
				textures.put("%texture%", modID + ":block/" + name + "_edge_" + i);
				pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_UP_QUAD, textures);
				model = ModelsHelper.fromPattern(pattern);
				modelCache.put(keyQuad[i], model);
			}
		}
		
		MultiPartBuilder builder = MultiPartBuilder.create(stateDefinition);
		appendCentralModel(stateId, builder, keyCube, modelCache);
		//builder.part(keyCube).add();
		
		// UP and DOWN
		Transformation transformation = new Transformation(null, Direction.DOWN.getRotation(), null, null);
		for (byte index = 0; index < 4; index++) {
			final BooleanProperty side = EdenBlockProperties.DIRECTIONS_HORIZONTAL[index];
			builder
				.part(keyQuad[index])
				.setCondition(state -> !state.getValue(BlockStateProperties.UP) && !state.getValue(side))
				.add();
			
			final BooleanProperty side2 = EdenBlockProperties.DIRECTIONS_HORIZONTAL[(2 - index) & 3];
			builder
				.part(keyQuad[index])
				.setTransformation(transformation)
				.setCondition(state -> !state.getValue(BlockStateProperties.DOWN) && !state.getValue(side2))
				.add();
		}
		
		// NORTH and SOUTH
		BooleanProperty[] directionsSide = new BooleanProperty[] {
			BlockStateProperties.UP,
			BlockStateProperties.WEST,
			BlockStateProperties.DOWN,
			BlockStateProperties.EAST
		};

		var north = Direction.NORTH.step();
		Vector3f offset = new Vector3f(north.x, north.y, north.z);
		offset.mul(0.001F);
		transformation = new Transformation(offset, Direction.NORTH.getRotation(), null, null);

		var south = Direction.SOUTH.step();
		offset = new Vector3f(south.x, south.y, south.z);
		offset.mul(0.001F);
		Transformation transformation2 = new Transformation(offset, Direction.SOUTH.getRotation(), null, null);
		
		for (byte index = 0; index < 4; index++) {
			final BooleanProperty side = directionsSide[index];
			builder
				.part(keyQuad[index])
				.setTransformation(transformation)
				.setCondition(state -> !state.getValue(BlockStateProperties.NORTH) && !state.getValue(side))
				.add();
			
			final BooleanProperty side2 = directionsSide[(-index) & 3];
			builder
				.part(keyQuad[index])
				.setTransformation(transformation2)
				.setCondition(state -> !state.getValue(BlockStateProperties.SOUTH) && !state.getValue(side2))
				.add();
		}
		
		// EAST and WEST
		directionsSide = new BooleanProperty[] {
			BlockStateProperties.UP,
			BlockStateProperties.NORTH,
			BlockStateProperties.DOWN,
			BlockStateProperties.SOUTH
		};

		var east = Direction.EAST.step();
		offset = new Vector3f(east.x, east.y, east.z);
		offset.mul(0.002F);
		transformation = new Transformation(offset, Direction.EAST.getRotation(), null, null);

		var west = Direction.WEST.step();
		offset = new Vector3f(west.x, west.y, west.z);
		offset.mul(0.002F);
		transformation2 = new Transformation(offset, Direction.WEST.getRotation(), null, null);
		
		for (byte index = 0; index < 4; index++) {
			final BooleanProperty side = directionsSide[index];
			builder
				.part(keyQuad[index])
				.setTransformation(transformation)
				.setCondition(state -> !state.getValue(BlockStateProperties.EAST) && !state.getValue(side))
				.add();
			
			final BooleanProperty side2 = directionsSide[(-index) & 3];
			builder
				.part(keyQuad[index])
				.setTransformation(transformation2)
				.setCondition(state -> !state.getValue(BlockStateProperties.WEST) && !state.getValue(side2))
				.add();
		}
		
		return builder.build();
	}
	
	@Environment(EnvType.CLIENT)
	protected void appendCentralModel(ResourceLocation stateId, MultiPartBuilder builder, ModelResourceLocation keyCube, Map<ResourceLocation, UnbakedModel> modelCache) {
		builder.part(keyCube).add();
	}
	
	private Axis getCrossAxis(Axis axis) {
		switch (axis) {
			case X -> { return Axis.Z; }
			case Z -> { return Axis.X; }
		}
		return Axis.X;
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
}
