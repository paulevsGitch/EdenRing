package paulevs.edenring.world;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import paulevs.edenring.blocks.EdenBlockProperties;
import paulevs.edenring.blocks.EdenBlockProperties.EdenPortalState;
import paulevs.edenring.registries.EdenBlocks;
import ru.bclib.util.BlocksHelper;

import java.util.Map;

public class EdenPortal {
	private static final Map<BlockPos, BlockState> PRE_PORTAL = Maps.newHashMap();
	private static final Map<BlockPos, BlockState> PORTAL = Maps.newHashMap();
	
	public static void init() {}
	
	public static void destroyPortal(Level level, BlockPos center) {
		BlockState air = Blocks.AIR.defaultBlockState();
		PORTAL.forEach((pos, state) -> {
			BlockPos offsetted = pos.offset(center);
			BlockState stateWorld = level.getBlockState(offsetted);
			if (stateWorld.equals(state)) {
				BlockState replacement = PRE_PORTAL.getOrDefault(pos, air);
				if (!(stateWorld.equals(replacement))) {
					BlocksHelper.setWithoutUpdate(level, offsetted, replacement);
				}
			}
		});
	}
	
	public static boolean checkOldPortal(Level level, BlockPos center) {
		boolean[] isCorrect = new boolean[] {true};
		PORTAL.forEach((pos, state) -> {
			BlockPos offsetted = pos.offset(center);
			BlockState stateWorld = level.getBlockState(offsetted);
			if (!stateWorld.equals(state)) {
				isCorrect[0] = false;
			}
		});
		return isCorrect[0];
	}
	
	public static boolean checkNewPortal(Level level, BlockPos center) {
		boolean[] isCorrect = new boolean[] {true};
		PRE_PORTAL.forEach((pos, state) -> {
			BlockPos offsetted = pos.offset(center);
			BlockState stateWorld = level.getBlockState(offsetted);
			if (!stateWorld.equals(state)) {
				isCorrect[0] = false;
			}
		});
		return isCorrect[0];
	}
	
	public static void buildPortal(Level level, BlockPos center) {
		PORTAL.forEach((pos, state) -> {
			BlocksHelper.setWithoutUpdate(level, pos.offset(center), state);
		});
	}
	
	static {
		EnumProperty<EdenPortalState> portalProperty = EdenBlockProperties.EDEN_PORTAL;
		BlockState portal = EdenBlocks.PORTAL_BLOCK.defaultBlockState();
		BlockState pillarBottom = portal.setValue(portalProperty, EdenPortalState.PILLAR_BOTTOM);
		BlockState pillarTop = portal.setValue(portalProperty, EdenPortalState.PILLAR_TOP);
		
		PORTAL.put(new BlockPos(-2, -1, -2), pillarBottom);
		PORTAL.put(new BlockPos(-2, 0, -2), pillarTop);
		PORTAL.put(new BlockPos(2, -1, -2), pillarBottom);
		PORTAL.put(new BlockPos(2, 0, -2), pillarTop);
		PORTAL.put(new BlockPos(-2, -1, 2), pillarBottom);
		PORTAL.put(new BlockPos(-2, 0, 2), pillarTop);
		PORTAL.put(new BlockPos(2, -1, 2), pillarBottom);
		PORTAL.put(new BlockPos(2, 0, 2), pillarTop);
		
		PORTAL.put(BlockPos.ZERO, EdenBlocks.PORTAL_CENTER.defaultBlockState());
		
		BlockPos below = new BlockPos(0, -1, 0);
		PORTAL.put(below, portal.setValue(portalProperty, EdenPortalState.CENTER_MIDDLE));
		PORTAL.put(below.north(), portal.setValue(portalProperty, EdenPortalState.CENTER_N));
		PORTAL.put(below.north().east(), portal.setValue(portalProperty, EdenPortalState.CENTER_NE));
		PORTAL.put(below.east(), portal.setValue(portalProperty, EdenPortalState.CENTER_E));
		PORTAL.put(below.south().east(), portal.setValue(portalProperty, EdenPortalState.CENTER_SE));
		PORTAL.put(below.south(), portal.setValue(portalProperty, EdenPortalState.CENTER_S));
		PORTAL.put(below.south().west(), portal.setValue(portalProperty, EdenPortalState.CENTER_SW));
		PORTAL.put(below.west(), portal.setValue(portalProperty, EdenPortalState.CENTER_W));
		PORTAL.put(below.north().west(), portal.setValue(portalProperty, EdenPortalState.CENTER_NW));
		
		DirectionProperty facing = HorizontalDirectionalBlock.FACING;
		BlockState stairs = Blocks.WAXED_CUT_COPPER_STAIRS.defaultBlockState();
		for (int i = -1; i < 2; i++) {
			PORTAL.put(below.north(2).east(i), stairs.setValue(facing, Direction.SOUTH));
			PORTAL.put(below.south(2).east(i), stairs.setValue(facing, Direction.NORTH));
			PORTAL.put(below.east(2).north(i), stairs.setValue(facing, Direction.WEST));
			PORTAL.put(below.west(2).north(i), stairs.setValue(facing, Direction.EAST));
		}
		
		BlockPos above = new BlockPos(0, 1, 0);
		facing = BlockStateProperties.FACING;
		BlockState amethystCluster = Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(facing, Direction.UP);
		PORTAL.put(above.north(2).west(2), amethystCluster);
		PORTAL.put(above.north(2).east(2), amethystCluster);
		PORTAL.put(above.south(2).west(2), amethystCluster);
		PORTAL.put(above.south(2).east(2), amethystCluster);
		
		BlockState copperBlock = Blocks.WAXED_COPPER_BLOCK.defaultBlockState();
		BlockState amethystBlock = Blocks.AMETHYST_BLOCK.defaultBlockState();
		PORTAL.forEach((pos, state) -> {
			if (pos.getY() < 0) {
				PRE_PORTAL.put(pos, state.is(EdenBlocks.PORTAL_BLOCK) ? copperBlock : state);
			}
			else if (state.is(EdenBlocks.PORTAL_BLOCK)) {
				PRE_PORTAL.put(pos, amethystBlock);
			}
			else if (state.is(Blocks.AMETHYST_CLUSTER)) {
				PRE_PORTAL.put(pos, state);
			}
		});
		
		PRE_PORTAL.put(below, Blocks.GOLD_BLOCK.defaultBlockState());
	}
	
}
