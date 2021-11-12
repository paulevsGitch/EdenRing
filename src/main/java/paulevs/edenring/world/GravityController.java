package paulevs.edenring.world;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.blocks.BlockProperties;

public class GravityController {
	public static double getGravityMultiplier(double y) {
		return Mth.lerp(Math.abs(y - 128.0) * 0.007, 1.0, 0.2);
	}
	
	public static double getGraviliteMultiplier(Entity entity) {
		MutableBlockPos pos = entity.blockPosition().mutable();
		int dist = 8;
		for (int i = 0; i < 8; i++) {
			if (entity.level.getBlockState(pos).is(EdenBlocks.GRAVILITE_BLOCK)) {
				dist = i;
				break;
			}
			pos.setY(pos.getY() - 1);
		}
		if (dist == 8) {
			return 1.0;
		}
		float delta = Mth.clamp((dist + (float) (entity.getY() - (int) entity.getY())) / 8F, 0, 1);
		return Mth.lerp(delta, 0.1, 1.0);
	}
	
	public static double getCompressorMultiplier(Entity entity) {
		MutableBlockPos pos = entity.blockPosition().mutable();
		boolean isActive = false;
		int dist = 64;
		for (int i = 0; i < 64; i++) {
			BlockState state = entity.level.getBlockState(pos);
			if (state.is(EdenBlocks.GRAVITY_COMPRESSOR)) {
				isActive = state.getValue(BlockProperties.ACTIVE);
				dist = i;
				break;
			}
			pos.setY(pos.getY() - 1);
		}
		if (dist == 64) {
			return 1.0;
		}
		float delta = Mth.clamp((dist + (float) (entity.getY() - (int) entity.getY())) / 64F, 0, 1);
		return isActive ? Mth.lerp(delta, -1.0, 1.0) : Mth.lerp(delta, 0.1, 1.0);
	}
}
