package paulevs.edenring.world;

import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import paulevs.edenring.registries.EdenBlocks;

public class GravityController {
	public static double getGravityMultiplier(double y) {
		double gravity = Mth.lerp(Math.abs(y - 128.0) * 0.007, 1.0, 0.2);
		return y > 256 ? Math.max(gravity, 0.2) : gravity;
	}
	
	public static double getGraviliteMultiplier(Entity entity) {
		MutableBlockPos pos = entity.blockPosition().mutable();
		int dist = 8;
		for (int i = 0; i < 8; i++) {
			if (entity.level().getBlockState(pos).is(EdenBlocks.GRAVILITE_BLOCK)) {
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
		int power = 0;
		int dist = 64;
		for (int i = 0; i < 64; i++) {
			BlockState state = entity.level().getBlockState(pos);
			if (state.is(EdenBlocks.GRAVITY_COMPRESSOR)) {
				power = state.getValue(BlockStateProperties.POWER);
				dist = i;
				break;
			}
			pos.setY(pos.getY() - 1);
		}
		if (dist == 64) {
			return 1.0;
		}
		float delta = Mth.clamp((dist + (float) (entity.getY() - (int) entity.getY())) / 64F, 0, 1);
		delta = (1 - delta) * (power / 15F);
		return power > 0 ? Mth.lerp(delta, 1.0, -1.0) : Mth.lerp(delta, 1.0, 0.1);
	}
}
