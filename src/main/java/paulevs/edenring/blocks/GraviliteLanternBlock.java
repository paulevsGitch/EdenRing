package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseAttachedBlock;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.tools.AddMineableHammer;
import org.betterx.bclib.interfaces.tools.AddMineablePickaxe;
import paulevs.edenring.EdenRing;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class GraviliteLanternBlock extends BaseAttachedBlock implements BlockModelProvider,
		AddMineableHammer, AddMineablePickaxe {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	
	public GraviliteLanternBlock() {
		super(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(15));
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Direction facing = blockState.getValue(FACING);
		Transformation transformation = new Transformation(null, facing.getRotation(), null, null);
		return ModelsHelper.createMultiVariant(EdenRing.makeID("block/gravilite_lantern"), transformation, false);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/gravilite_lantern.json"), Maps.newHashMap());
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return BOUNDING_SHAPES.get(state.getValue(FACING));
	}
	
	static {
		BOUNDING_SHAPES.put(Direction.UP, box(4, 0, 4, 12, 8, 12));
		BOUNDING_SHAPES.put(Direction.DOWN, box(4, 8, 4, 12, 16, 12));
		BOUNDING_SHAPES.put(Direction.NORTH, box(4, 4, 8, 12, 12, 16));
		BOUNDING_SHAPES.put(Direction.SOUTH, box(4, 4, 0, 12, 12, 8));
		BOUNDING_SHAPES.put(Direction.WEST, box(8, 4, 4, 16, 12, 12));
		BOUNDING_SHAPES.put(Direction.EAST, box(0, 4, 4, 8, 12, 12));
	}
}
