package paulevs.edenring.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.CustomColorProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.ui.ColorUtil;

import java.util.Map;
import java.util.Optional;

public class EdenMossBlock extends MossBlock implements BlockModelProvider, CustomColorProvider, RenderLayerProvider {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	
	public EdenMossBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MOSS_BLOCK).sound(SoundType.MOSS).offsetType(OffsetType.NONE).noCollission());
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos below = pos.below();
		BlockState down = world.getBlockState(below);
		return down.isFaceSturdy(world, below, Direction.UP) || down.is(BlockTags.LEAVES);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String modId = blockId.getNamespace();
		String name = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%side%", modId + ":block/" + name + "_side");
		textures.put("%top%", modId + ":block/" + name + "_top");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.BLOCK_MOSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		String modId = itemID.getNamespace();
		String name = itemID.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%side%", modId + ":block/" + name + "_side");
		textures.put("%top%", modId + ":block/" + name + "_top");
		Optional<String> pattern = PatternsHelper.createJson(EdenPatterns.ITEM_MOSS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(), "block/" + stateId.getPath());
		this.registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createRandomTopModel(modelId);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockColor getProvider() {
		return (blockState, blockAndTintGetter, blockPos, i) -> {
			if (blockAndTintGetter == null || blockPos == null) {
				return GrassColor.get(0.5D, 1.0D);
			}
			int color = BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos);
			int[] rgba = ColorUtil.toIntArray(color);
			float[] hsv = ColorUtil.RGBtoHSB(rgba[1], rgba[2], rgba[3], null);
			hsv[0] += 0.02F;
			return ColorUtil.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
		};
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemColor getItemProvider() {
		return (itemStack, i) -> GrassColor.get(0.5D, 1.0D);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
	}
}
