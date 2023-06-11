package paulevs.edenring;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.betterx.bclib.api.v2.datafixer.DataFixerAPI;
import org.betterx.bclib.api.v2.datafixer.ForcedLevelPatch;
import org.betterx.bclib.api.v2.datafixer.MigrationProfile;
import org.betterx.bclib.creativetab.BCLCreativeTabManager;
import org.betterx.bclib.registry.BaseRegistry;
import org.betterx.worlds.together.util.Logger;
import paulevs.edenring.config.Configs;
import paulevs.edenring.paintings.EdenPaintings;
import paulevs.edenring.registries.*;
import paulevs.edenring.world.EdenPortal;
import paulevs.edenring.world.generator.EdenBiomeSource;
import paulevs.edenring.world.generator.GeneratorOptions;

public class EdenRing implements ModInitializer {
	public static final String MOD_ID = "edenring";
	public static final Logger LOGGER = new Logger(MOD_ID);

	public static final ResourceKey<DimensionType> EDEN_RING_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, makeID(MOD_ID));
	public static final ResourceKey<Level> EDEN_RING_KEY = ResourceKey.create(Registries.DIMENSION, makeID(MOD_ID));


	@Override
	public void onInitialize() {

		BCLCreativeTabManager.create(EdenRing.MOD_ID)
				.createTab("eden_tab")
				.setPredicate(
						item -> BaseRegistry.getModBlockItems(MOD_ID).contains(item)
								|| BaseRegistry.getModItems(MOD_ID).contains(item)
				)
				.setIcon(EdenBlocks.MOSSY_STONE)
				.build()
				.processBCLRegistry()
				.register();

		GeneratorOptions.init();
		EdenSounds.init();
		EdenBlocks.init();
		EdenBlockEntities.init();
		EdenPaintings.init();
		EdenEntities.init();
		EdenItems.init();
		EdenBiomes.register();
		EdenFeatures.register();
		EdenRecipes.init();
		Configs.saveConfigs();
		
		Registry.register(BuiltInRegistries.BIOME_SOURCE, makeID("biome_source"), EdenBiomeSource.CODEC);
		EdenPortal.init();
		
		DataFixerAPI.registerPatch(() -> new ForcedLevelPatch(MOD_ID, "0.2.0") {
			@Override
			protected Boolean runLevelDatPatch(CompoundTag root, MigrationProfile profile) {
				CompoundTag worldGenSettings = root.getCompound("Data").getCompound("WorldGenSettings");
				CompoundTag dimensions = worldGenSettings.getCompound("dimensions");
				String dimensionKey = EDEN_RING_KEY.location().toString();
				
				if (!dimensions.contains(dimensionKey)) {
					long seed = worldGenSettings.getLong("seed");
					CompoundTag dimRoot = new CompoundTag();
					dimensions.put(dimensionKey, dimRoot);
					dimRoot.putString("type", dimensionKey);
					
					CompoundTag generator = new CompoundTag();
					dimRoot.put("generator", generator);
					
					generator.putString("settings", "minecraft:floating_islands");
					generator.putString("type", "minecraft:noise");
					generator.putLong("seed", seed);
					
					CompoundTag biomeSource = new CompoundTag();
					generator.put("biome_source", biomeSource);
					biomeSource.putString("type", "edenring:biome_source");
					
					return true;
				}
				else {
					CompoundTag dimRoot = dimensions.getCompound(dimensionKey);
					CompoundTag generator = dimRoot.getCompound("generator");
					String settings = generator.getString("settings");
					if (!settings.equals("minecraft:floating_islands")) {
						generator.putString("settings", "minecraft:floating_islands");
						return true;
					}
				}
				
				return false;
			}
		});
		
		final ResourceLocation[] possibleLocations = new ResourceLocation[] {
			new ResourceLocation("chests/end_city_treasure"),
			new ResourceLocation("chests/buried_treasure"),
			new ResourceLocation("chests/desert_pyramid"),
			new ResourceLocation("chests/igloo_chest"),
			new ResourceLocation("chests/jungle_temple"),
			new ResourceLocation("chests/pillager_outpost"),
			new ResourceLocation("chests/shipwreck_treasure"),
			new ResourceLocation("chests/simple_dungeon")
		};
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, setter) -> {
			for (ResourceLocation resourceLocation: possibleLocations) {
				if (id.equals(resourceLocation)) {
					LootPool.Builder builder = LootPool.lootPool();
					builder.setRolls(ConstantValue.exactly(1));
					builder.conditionally(LootItemRandomChanceCondition.randomChance(0.4f).build());
					builder.add(LootItem.lootTableItem(EdenItems.GUIDE_BOOK));
					table.withPool(builder);
					break;
				}
			}
		});
	}
	
	public static ResourceLocation makeID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
