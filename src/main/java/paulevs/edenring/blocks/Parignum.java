package paulevs.edenring.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.EdenRing;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.interfaces.BlockModelProvider;
import ru.bclib.util.BlocksHelper;

import java.util.Map;
import java.util.Optional;

public class Parignum extends SixSidePlant implements BlockModelProvider {
	public Parignum() {
		super(FabricBlockSettings.copyOf(Blocks.VINE));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			ModelResourceLocation noFlowers = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "no_flowers_" + dir.name());
			ModelResourceLocation flowers1 = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "flowers_1_" + dir.name());
			ModelResourceLocation flowers2 = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "flowers_2_" + dir.name());
			ModelResourceLocation flowers3 = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "flowers_3_" + dir.name());
			ModelResourceLocation flowers4 = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), "flowers_4_" + dir.name());
			
			modelCache.put(noFlowers, makeModel(stateId, null));
			modelCache.put(flowers1, makeModel(stateId, "_flowers_1"));
			modelCache.put(flowers2, makeModel(stateId, "_flowers_2"));
			modelCache.put(flowers3, makeModel(stateId, "_flowers_3"));
			modelCache.put(flowers4, makeModel(stateId, "_flowers_4"));
			
			Transformation transformation = new Transformation(null, dir.getOpposite().getRotation(), null, null);
			
			ModelResourceLocation stateModel = new ModelResourceLocation(stateId.getNamespace(), stateId.getPath(), dir.getName());
			modelCache.put(stateModel, new MultiVariant(Lists.newArrayList(
				new Variant(noFlowers, transformation, false, 2),
				new Variant(flowers1, transformation, false, 1),
				new Variant(flowers2, transformation, false, 1),
				new Variant(flowers3, transformation, false, 1),
				new Variant(flowers4, transformation, false, 1)
			)));
			
			int index = dir.get3DDataValue();
			model.part(stateModel).setCondition(state -> state.getValue(DIRECTIONS[index])).add();
		}
		
		return model.build();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%texture%", modId + ":block/" + name);
		textures.put("%overlay%", modId + ":block/" + name + "_flowers_1");
		Optional<String> pattern = PatternsHelper.createJson(EdenRing.makeID("patterns/item/tinted_overlay.json"), textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Environment(EnvType.CLIENT)
	private BlockModel makeModel(ResourceLocation stateId, String overlay) {
		Map<String, String> textures = Maps.newHashMap();
		ResourceLocation patternID;
		if (overlay == null) {
			textures.put("%texture%", stateId.getNamespace() + ":block/" + stateId.getPath());
			patternID = EdenRing.makeID("patterns/block/plane_tint.json");
		}
		else {
			textures.put("%texture%", stateId.getNamespace() + ":block/" + stateId.getPath());
			textures.put("%overlay%", stateId.getNamespace() + ":block/" + stateId.getPath() + overlay);
			patternID = EdenRing.makeID("patterns/block/plane_overlay.json");
		}
		Optional<String> pattern = PatternsHelper.createJson(patternID, textures);
		return ModelsHelper.fromPattern(pattern);
	}
}
