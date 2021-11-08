package paulevs.edenring;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenBlockEntities;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenRecipes;
import paulevs.edenring.world.EdenPortal;
import paulevs.edenring.world.features.EdenFeatures;
import paulevs.edenring.world.generator.EdenBiomeSource;
import paulevs.edenring.world.generator.EdenChunkGenerator;
import paulevs.edenring.world.generator.GeneratorOptions;
import ru.bclib.registry.BaseRegistry;

public class EdenRing implements ModInitializer {
	public static final String MOD_ID = "edenring";
	
	public static final ResourceKey<DimensionType> EDEN_RING_TYPE_KEY = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, makeID(MOD_ID));
	public static final ResourceKey<Level> EDEN_RING_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, makeID(MOD_ID));
	public static final CreativeModeTab EDEN_TAB = FabricItemGroupBuilder
		.create(makeID("eden_tab"))
		.icon(() -> new ItemStack(EdenBlocks.MOSSY_STONE))
		.appendItems(stacks -> stacks.addAll(BaseRegistry.getModBlocks(MOD_ID).stream().map(ItemStack::new).toList()))
		.build();
	
	@Override
	public void onInitialize() {
		GeneratorOptions.init();
		EdenBlocks.init();
		EdenBlockEntities.init();
		EdenFeatures.init();
		EdenBiomes.init();
		EdenRecipes.init();
		Registry.register(Registry.CHUNK_GENERATOR, makeID("chunk_generator"), EdenChunkGenerator.CODEC);
		Registry.register(Registry.BIOME_SOURCE, makeID("biome_source"), EdenBiomeSource.CODEC);
		EdenPortal.init();
	}
	
	public static ResourceLocation makeID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
