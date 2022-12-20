package paulevs.edenring.registries;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.betterx.bclib.api.v2.spawning.SpawnRuleBuilder;
import org.betterx.bclib.config.PathConfig;
import paulevs.edenring.EdenRing;
import paulevs.edenring.entities.DiskwingEntity;
import paulevs.edenring.entities.EdenPainting;
import paulevs.edenring.entities.LightningRayEntity;

public class EdenEntities {
	private static final PathConfig ENTITY_CONFIG = new PathConfig(EdenRing.MOD_ID, "entities");
	
	// Living //
	public static final EntityType<DiskwingEntity> DISKWING = register("diskwing", MobCategory.AMBIENT, 0.9F, 0.25F, DiskwingEntity::new, DiskwingEntity.createMobAttributes(), 0x5b3e52, 0x978090);
	
	// Technical //
	public static final EntityType<LightningRayEntity> LIGHTNING_RAY = register("lightning_ray", MobCategory.MISC, 1.0F, 1.0F, LightningRayEntity::new);
	public static final EntityType<EdenPainting> LIMPHIUM_PAINTING = register("limphium_painting", FabricEntityTypeBuilder
		.create(MobCategory.MISC, (EntityFactory<EdenPainting>) EdenPainting::new)
		.dimensions(EntityDimensions.fixed(0.5F, 0.5F))
		.trackedUpdateRate(Integer.MAX_VALUE)
		.trackRangeBlocks(10)
		.disableSummon()
		.build()
	);
	
	public static void init() {
		SpawnRuleBuilder.start(DISKWING).maxNearby(8).buildNoRestrictions(Types.MOTION_BLOCKING);
	}
	
	protected static <T extends Entity> EntityType<T> register(String name, MobCategory group, float width, float height, EntityFactory<T> entity) {
		EntityType<T> type = FabricEntityTypeBuilder.create(group, entity).disableSummon().dimensions(EntityDimensions.fixed(width, height)).build();
		return register(name, type);
	}
	
	protected static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
		ResourceLocation id = EdenRing.makeID(name);
		if (ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);
		}
		return type;
	}
	
	protected static <T extends Mob> EntityType<T> register(String name, MobCategory group, float width, float height, EntityFactory<T> entity, AttributeSupplier.Builder attributes, int eggColor, int dotsColor) {
		ResourceLocation id = EdenRing.makeID(name);
		EntityType<T> type = FabricEntityTypeBuilder.create(group, entity).dimensions(EntityDimensions.fixed(width, height)).build();
		if (ENTITY_CONFIG.getBooleanRoot(id.getPath(), true)) {
			Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);
			FabricDefaultAttributeRegistry.register(type, attributes);
			EdenItems.REGISTRY.registerEgg(EdenRing.makeID("spawn_egg_" + name), type, eggColor, dotsColor);
		}
		return type;
	}
}
