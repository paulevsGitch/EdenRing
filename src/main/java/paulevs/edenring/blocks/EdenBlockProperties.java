package paulevs.edenring.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import ru.bclib.blocks.BlockProperties;

public class EdenBlockProperties extends BlockProperties {
	public static final EnumProperty<BalloonMushroomStemState> BALLOON_MUSHROOM_STEM = EnumProperty.create("shape", BalloonMushroomStemState.class);
	
	public enum BalloonMushroomStemState implements StringRepresentable {
		UP("up"),
		NORTH_SOUTH("north_south"),
		EAST_WEST("east_west"),
		THIN("thin"),
		THIN_TOP("thin_top"),
		FUR("fur");
		
		private final String name;
		
		BalloonMushroomStemState(String name) {
			this.name = name;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
