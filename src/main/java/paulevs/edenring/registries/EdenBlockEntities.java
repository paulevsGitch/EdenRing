package paulevs.edenring.registries;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import paulevs.edenring.EdenRing;
import paulevs.edenring.blocks.entities.EdenPortalBlockEntity;

public class EdenBlockEntities {
	public final static BlockEntityType<EdenPortalBlockEntity> EDEN_PORTAL = register("eden_portal", FabricBlockEntityTypeBuilder.create(EdenPortalBlockEntity::new, EdenBlocks.PORTAL_CENTER));
	
	private static <T extends BlockEntity> BlockEntityType<T> register(String id, FabricBlockEntityTypeBuilder<T> builder) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, EdenRing.makeID(id), builder.build(null));
	}
	
	public static void init() {}
}