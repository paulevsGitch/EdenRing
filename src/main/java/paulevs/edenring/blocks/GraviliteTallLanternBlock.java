package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import paulevs.edenring.EdenRing;
import ru.bclib.blocks.BaseRotatedPillarBlock;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class GraviliteTallLanternBlock extends BaseRotatedPillarBlock {
	private static final EnumMap<Axis, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Axis.class);
	
	public GraviliteTallLanternBlock() {
		super(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(15));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Axis axis = blockState.getValue(AXIS);
		return ModelsHelper.createRotatedModel(EdenRing.makeID("block/gravilite_lantern_tall"), axis);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("models/block/gravilite_lantern_tall.json"), Maps.newHashMap());
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return BOUNDING_SHAPES.get(state.getValue(AXIS));
	}
	
	static {
		BOUNDING_SHAPES.put(Axis.X, box(0, 4, 4, 16, 12, 12));
		BOUNDING_SHAPES.put(Axis.Y, box(4, 0, 4, 12, 16, 12));
		BOUNDING_SHAPES.put(Axis.Z, box(4, 4, 0, 12, 12, 16));
	}
}
