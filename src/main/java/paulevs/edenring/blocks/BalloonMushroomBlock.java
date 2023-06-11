package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import org.betterx.bclib.client.models.BasePatterns;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.ModelsHelper.MultiPartBuilder;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.interfaces.CustomItemProvider;
import paulevs.edenring.BaseCTBlock;
import paulevs.edenring.items.BalloonMushroomBlockItem;

import java.util.Map;
import java.util.Optional;

public class BalloonMushroomBlock extends BaseCTBlock implements CustomItemProvider {
	public static final BooleanProperty NATURAL = EdenBlockProperties.NATURAL;
	
	public BalloonMushroomBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MUSHROOM_STEM).mapColor(MapColor.COLOR_PURPLE));
		registerDefaultState(defaultBlockState().setValue(NATURAL, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		super.createBlockStateDefinition(stateManager);
		stateManager.add(NATURAL);
	}
	
	@Override
	public BlockItem getCustomItem(ResourceLocation resourceLocation, Item.Properties itemProperties) {
		return new BalloonMushroomBlockItem(this, itemProperties);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	protected void appendCentralModel(ResourceLocation stateId, MultiPartBuilder builder, ModelResourceLocation keyCube, Map<ResourceLocation, UnbakedModel> modelCache) {
		builder.part(keyCube).setCondition(state -> !state.getValue(NATURAL)).add();
		
		keyCube = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), defaultBlockState().toString() + "_natural");
		if (!modelCache.containsKey(keyCube)) {
			Map<String, String> textures = Maps.newHashMap();
			String side = stateId.getNamespace() + ":block/" + stateId.getPath();
			textures.put("%bottom%", side + "_bottom");
			textures.put("%side%", side);
			textures.put("%top%", side);
			Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_TOP_SIDE_BOTTOM, textures);
			
			BlockModel model = ModelsHelper.fromPattern(pattern);
			modelCache.put(keyCube, model);
		}
		
		builder.part(keyCube).setCondition(state -> state.getValue(NATURAL)).add();
	}
}
