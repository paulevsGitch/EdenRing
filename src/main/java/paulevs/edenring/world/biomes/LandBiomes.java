package paulevs.edenring.world.biomes;

import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBlocks;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenParticles;
import paulevs.edenring.registries.EdenSounds;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.world.biomes.BCLBiome;

public class LandBiomes {
	public static BCLBiome makeStoneGardenBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("stone_garden"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		return builder
			.plantsColor(162, 190, 113)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.FOREST_ROCK)
			.feature(EdenFeatures.COBBLE_FLOOR)
			.feature(EdenFeatures.MOSS_FLOOR)
			.feature(EdenFeatures.MOSS_LAYER)
			.feature(EdenFeatures.EDEN_MOSS_LAYER)
			.feature(EdenFeatures.STONE_PILLAR)
			.feature(EdenFeatures.LIMPHIUM)
			.feature(EdenFeatures.VIOLUM_RARE)
			.feature(EdenFeatures.EDEN_VINE)
			.feature(EdenFeatures.ROOTS)
			.feature(EdenFeatures.PARIGNUM)
			.build();
	}
	
	public static BCLBiome makeGoldenForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("golden_forest"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		return builder
			.plantsColor(255, 174, 100)
			.skyColor(113, 178, 255)
			.fogColor(183, 212, 255)
			.loop(EdenSounds.AMBIENCE_GOLDEN_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
			.feature(EdenFeatures.AURITIS_TREE)
			.feature(EdenFeatures.EDEN_MOSS_LAYER)
			.feature(EdenFeatures.GOLDEN_GRASS)
			.feature(EdenFeatures.EDEN_VINE)
			.feature(EdenFeatures.ORE_GOLD)
			.feature(EdenFeatures.PARIGNUM)
			.build();
	}
	
	public static BCLBiome makeMycoticForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("mycotic_forest"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(EdenBlocks.EDEN_MYCELIUM.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
		
		return builder
			.grassColor(220, 130, 189)
			.foliageColor(152, 90, 131)
			.skyColor(113, 178, 255)
			.fogColor(178, 112, 143)
			.loop(EdenSounds.AMBIENCE_MYCOTIC_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(EdenFeatures.BALLOON_MUSHROOM_TREE)
			.feature(EdenFeatures.TALL_BALLOON_MUSHROOM)
			.feature(EdenFeatures.BALLOON_MUSHROOM_SMALL)
			.feature(EdenFeatures.GRASS_FLOOR)
			.feature(EdenFeatures.LIMPHIUM)
			.feature(EdenFeatures.TALL_MYCOTIC_GRASS)
			.feature(EdenFeatures.MYCOTIC_GRASS)
			.feature(EdenFeatures.EDEN_VINE)
			.build();
	}
	
	public static BCLBiome makeOldMycoticForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("old_mycotic_forest"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		builder.surface(SurfaceRuleBuilder
			.start()
			.surface(EdenBlocks.EDEN_MYCELIUM.defaultBlockState())
			.subsurface(Blocks.DIRT.defaultBlockState(), 3)
			.build()
		);
		
		return builder
			.grassColor(220, 130, 189)
			.foliageColor(152, 90, 131)
			.skyColor(113, 178, 255)
			.fogColor(178, 112, 143)
			.loop(EdenSounds.AMBIENCE_MYCOTIC_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(EdenFeatures.OLD_BALLOON_MUSHROOM_TREE)
			.feature(EdenFeatures.BALLOON_MUSHROOM_TREE)
			.feature(EdenFeatures.TALL_BALLOON_MUSHROOM)
			.feature(EdenFeatures.BALLOON_MUSHROOM_SMALL)
			.feature(EdenFeatures.LIMPHIUM)
			.feature(EdenFeatures.TALL_MYCOTIC_GRASS)
			.feature(EdenFeatures.MYCOTIC_GRASS)
			.feature(EdenFeatures.EDEN_VINE)
			.genChance(0.5F)
			.build();
	}
	
	public static BCLBiome makePulseForestBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("pulse_forest"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		return builder
			.skyColor(113, 178, 255)
			.fogColor(115, 235, 242)
			.plantsColor(121, 238, 248)
			.loop(EdenSounds.AMBIENCE_PULSE_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN)
			.feature(EdenFeatures.PULSE_TREE)
			.feature(EdenFeatures.LIMPHIUM)
			.feature(EdenFeatures.VIOLUM_DENSE)
			.feature(EdenFeatures.EDEN_VINE)
			.build();
	}
	
	public static BCLBiome makeBrainstormBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("brainstorm"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		return builder
			.fogDensity(2.0F)
			.skyColor(113, 178, 255)
			.fogColor(180, 180, 180)
			.plantsColor(200, 200, 200)
			.loop(EdenSounds.AMBIENCE_BRAINSTORM)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
			.feature(EdenFeatures.TALL_COPPER_GRASS)
			.feature(EdenFeatures.TALL_IRON_GRASS)
			.feature(EdenFeatures.TALL_GOLD_GRASS)
			.feature(EdenFeatures.COPPER_GRASS)
			.feature(EdenFeatures.IRON_GRASS)
			.feature(EdenFeatures.GOLD_GRASS)
			.feature(EdenFeatures.BRAIN_TREE)
			.feature(EdenFeatures.LAYERED_IRON)
			.feature(EdenFeatures.LAYERED_COPPER)
			.feature(EdenFeatures.LAYERED_GOLD)
			.build();
	}
	
	public static BCLBiome makeLakesideDesertBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("lakeside_desert"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		BlockState sandstone = Blocks.SANDSTONE.defaultBlockState();
		builder.surface(SurfaceRuleBuilder
			.start()
			.subsurface(Blocks.SAND.defaultBlockState(), 3)
			.filler(sandstone)
			.ceil(sandstone)
			.build()
		);
		
		return builder
			.fogDensity(1.75F)
			.skyColor(113, 178, 255)
			.fogColor(237, 235, 203)
			.grassColor(246, 222, 173)
			.foliageColor(247, 165, 115)
			.loop(EdenSounds.AMBIENCE_LAKESIDE_DESSERT)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
			.feature(EdenFeatures.GRAVEL_FLOOR)
			.feature(EdenFeatures.GRASS_FLOOR)
			.feature(EdenFeatures.AQUATUS)
			.build();
	}
	
	public static BCLBiome makeWindValleyBiome() {
		BCLBiomeBuilder builder = BCLBiomeBuilder.start(EdenRing.makeID("wind_valley"));
		BiomesCommonMethods.addDefaultLandFeatures(builder);
		BiomesCommonMethods.addDefaultSurface(builder);
		BiomesCommonMethods.setDefaultColors(builder);
		BiomesCommonMethods.addDefaultSounds(builder);
		
		return builder
			.skyColor(113, 178, 255)
			.fogColor(183, 212, 255)
			.grassColor(225, 84, 72)
			.foliageColor(230, 63, 50)
			.loop(EdenSounds.AMBIENCE_WIND_VALLEY)
			.particles(EdenParticles.WIND_PARTICLE, 0.001F)
			.feature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
			.feature(EdenFeatures.VIOLUM_RARE)
			.feature(EdenFeatures.LONLIX)
			.feature(EdenFeatures.PARIGNUM)
			.build();
	}
}
