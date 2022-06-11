package paulevs.edenring.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.betterx.bclib.blocks.BlockProperties;

public class EdenBlockProperties extends BlockProperties {
	public static final EnumProperty<BalloonMushroomStemState> BALLOON_MUSHROOM_STEM = EnumProperty.create("shape", BalloonMushroomStemState.class);
	public static final EnumProperty<EdenPortalState> EDEN_PORTAL = EnumProperty.create("shape", EdenPortalState.class);
	public static final EnumProperty<PulseTreeState> PULSE_TREE = EnumProperty.create("shape", PulseTreeState.class);
	public static final EnumProperty<QuadShape> QUAD_SHAPE = EnumProperty.create("shape", QuadShape.class);
	public static final IntegerProperty TEXTURE_4 = IntegerProperty.create("texture", 0, 3);
	public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
	public static final BooleanProperty[] DIRECTIONS = new BooleanProperty[] {
		BlockStateProperties.DOWN,
		BlockStateProperties.UP,
		BlockStateProperties.NORTH,
		BlockStateProperties.SOUTH,
		BlockStateProperties.WEST,
		BlockStateProperties.EAST
	};
	public static final BooleanProperty[] DIRECTIONS_HORIZONTAL = new BooleanProperty[] {
		BlockStateProperties.NORTH,
		BlockStateProperties.EAST,
		BlockStateProperties.SOUTH,
		BlockStateProperties.WEST
	};
	
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
		UP("up", false),
		NORTH_SOUTH("north_south", false),
		EAST_WEST("east_west", false),
		HEAD_BIG("head_big", true),
		HEAD_MEDIUM("head_medium", true),
		HEAD_SMALL("head_small", true);
		
		private final String name;
		private final boolean natural;
		
		PulseTreeState(String name, boolean natural) {
			this.name = name;
			this.natural = natural;
		}
		
		@Override
		public String getSerializedName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		
		public boolean isNatural() {
			return natural;
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
	
	public enum QuadShape implements StringRepresentable {
		SMALL("small"),
		TOP("top"),
		MIDDLE("middle"),
		BOTTOM("bottom");
		
		private final String name;
		
		QuadShape(String name) {
			this.name = name;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
