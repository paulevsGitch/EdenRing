package paulevs.edenring.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.BrainTreeBlock;
import paulevs.edenring.blocks.entities.BrainTreeBlockEntity;
import ru.bclib.registry.BlockRegistry;

public class EdenBlockEntities {
	public final static BlockEntityType<BrainTreeBlockEntity> BRAIN_BLOCK = register("brain_block", FabricBlockEntityTypeBuilder.create(BrainTreeBlockEntity::new, getBrainBlocks()));
	
	private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, EdenRing.makeID(id), builder.build(null));
	}
	
	private static Block[] getBrainBlocks() {
		return BlockRegistry.getModBlocks(EdenRing.MOD_ID)
			.stream()
			.filter(block -> block instanceof BrainTreeBlock)
			.toList().toArray(new Block[0]);
	}
	
	public static void init() {}
}
