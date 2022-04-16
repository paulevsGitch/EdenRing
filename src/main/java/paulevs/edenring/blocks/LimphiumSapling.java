package paulevs.edenring.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

import java.util.Random;

public class LimphiumSapling extends OverlayPlantBlock {
	public LimphiumSapling() {
		super(FabricBlockSettings.copyOf(Blocks.GRASS).randomTicks());
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(8) == 0;
	}
	
	@Override
	public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
		grow(level, random, pos, state);
	}
	
	public static void grow(WorldGenLevel level, Random random, BlockPos pos, BlockState state) {
		byte h = (byte) MHelper.randRange(2, 4, random);
		MutableBlockPos p = pos.mutable().setY(pos.getY() + 1);
		for (byte i = 1; i < h; i++) {
			if (!level.getBlockState(p).isAir()) {
				h = i;
				break;
			}
			p.setY(p.getY() + 1);
		}
		if (h < 2) return;
		p.set(pos).setY(pos.getY() + 1);
		byte max = (byte) (h - 1);
		BlockState stem = EdenBlocks.LIMPHIUM.defaultBlockState().setValue(BlockStateProperties.HALF, Half.BOTTOM);
		BlockState top = EdenBlocks.LIMPHIUM.defaultBlockState().setValue(BlockStateProperties.HALF, Half.TOP);
		for (byte i = 1; i < max; i++) {
			BlocksHelper.setWithoutUpdate(level, p, stem);
			p.setY(p.getY() + 1);
		}
		BlocksHelper.setWithoutUpdate(level, p, top);
		BlocksHelper.setWithUpdate(level, pos, stem);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		this.tick(state, world, pos, random);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (isBonemealSuccess(world, random, pos, state)) {
			performBonemeal(world, random, pos, state);
		}
	}
}
