package paulevs.edenring.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.bclib.blocks.FeatureSaplingBlock;

import java.util.function.Supplier;

public class CustomSaplingBlock extends FeatureSaplingBlock {
	private final Supplier<Feature<?>> feature;
	
	public CustomSaplingBlock(Supplier<Feature<?>> feature) {
		super();
		this.feature = feature;
	}
	
	@Override
	protected Feature<?> getFeature(BlockState state) {
		return feature.get();
	}
}
