package paulevs.edenring.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import ru.bclib.blocks.BlockProperties;

public class EdenBlockProperties extends BlockProperties {
	public static final EnumProperty<BalloonMushroomStemState> BALLOON_MUSHROOM_STEM = EnumProperty.create("shape", BalloonMushroomStemState.class);
	public static final EnumProperty<EdenPortalState> EDEN_PORTAL = EnumProperty.create("shape", EdenPortalState.class);
	public static final EnumProperty<PulseTreeState> PULSE_TREE = EnumProperty.create("shape", PulseTreeState.class);
	
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
	
	public enum PulseTreeState implements StringRepresentable {
		UP("up"),
		NORTH_SOUTH("north_south"),
		EAST_WEST("east_west"),
		HEAD_BIG("head_big"),
		HEAD_MEDIUM("head_medium"),
		HEAD_SMALL("head_small");
		
		private final String name;
		
		PulseTreeState(String name) {
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
	
	public enum EdenPortalState implements StringRepresentable {
		PILLAR_TOP("pillar_top"),
		PILLAR_BOTTOM("pillar_bottom"),
		CENTER_NW("center_nw"),
		CENTER_N("center_n"),
		CENTER_NE("center_ne"),
		CENTER_W("center_w"),
		CENTER_MIDDLE("center_middle"),
		CENTER_E("center_e"),
		CENTER_SW("center_sw"),
		CENTER_S("center_s"),
		CENTER_SE("center_se");
		
		private final String name;
		
		EdenPortalState(String name) {
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
