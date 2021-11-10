package paulevs.edenring.registries;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import paulevs.edenring.EdenRing;
import paulevs.edenring.entities.LightningRayEntity;
import ru.bclib.config.PathConfig;

public class EdenEntities {
	private static final PathConfig ENTITY_CONFIG = new PathConfig(EdenRing.MOD_ID, "entities");
	
	public static final EntityType<LightningRayEntity> LIGHTNING_RAY = register("lightning_ray", MobCategory.MISC, 1.0F, 1.0F, LightningRayEntity::new);
	
	public static void init() {}
	
	protected static <T extends Entity> EntityType<T> register(String name, MobCategory group, float width, float height, EntityFactory<T> entity) {
		ResourceLocation id = EdenRing.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder.create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build();
		if (ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			Registry.register(Registry.ENTITY_TYPE, id, type);
		}
		return type;
	}
}
