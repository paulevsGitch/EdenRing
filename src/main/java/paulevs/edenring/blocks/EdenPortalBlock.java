package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.blocks.BaseBlockNotFull;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.EdenBlockProperties.EdenPortalState;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdenPortalBlock extends BaseBlockNotFull {
	public static final EnumProperty<EdenPortalState> EDEN_PORTAL = EdenBlockProperties.EDEN_PORTAL;
	private static final Map<EdenPortalState, VoxelShape> BOUNDS = Maps.newEnumMap(EdenPortalState.class);
	
	public EdenPortalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(EDEN_PORTAL);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		EdenPortalState shape = blockState.getValue(EDEN_PORTAL);
		String name = shape.getSerializedName();
		if (name.startsWith("center")) {
			String modId = stateId.getNamespace();
			Map<String, String> textures = Maps.newHashMap();
			textures.put("%top%", modId + ":block/portal_block_" + name);
			textures.put("%side%", "minecraft:block/copper_block");
			textures.put("%bottom%", "minecraft:block/copper_block");
			Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
			return ModelsHelper.fromPattern(pattern);
		}
		ResourceLocation modelId = EdenRing.makeID("block/portal_" + name);
		return ModelsHelper.createBlockSimple(modelId);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		VoxelShape shape = BOUNDS.get(state.getValue(EDEN_PORTAL));
		return shape == null ? Shapes.block() : shape;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		Block drop = Blocks.WAXED_COPPER_BLOCK;
		if (state.getValue(EDEN_PORTAL) == EdenPortalState.PILLAR_TOP) drop = Blocks.AMETHYST_BLOCK;
		else if (state.getValue(EDEN_PORTAL) == EdenPortalState.CENTER_MIDDLE) drop = Blocks.GOLD_BLOCK;
		return Collections.singletonList(new ItemStack(drop));
	}
	
	static {
		BOUNDS.put(EdenPortalState.PILLAR_TOP, box(2, 0, 2, 14, 16, 14));
		BOUNDS.put(EdenPortalState.PILLAR_BOTTOM, Shapes.or(box(2, 8, 2, 14, 16, 14), box(0, 0, 0, 16, 8, 16)));
	}
}
