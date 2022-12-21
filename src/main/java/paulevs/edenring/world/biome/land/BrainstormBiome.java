package paulevs.edenring.world.biome.land;

import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenFeatures;
import paulevs.edenring.registries.EdenSounds;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class BrainstormBiome extends EdenRingBiome.Config {
    public BrainstormBiome() {
        super(EdenBiomes.BRAINSTORM.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder
                .fogDensity(2.0F)
                .skyColor(113, 178, 255)
                .fogColor(180, 180, 180)
                .plantsColor(200, 200, 200)
                .loop(EdenSounds.AMBIENCE_BRAINSTORM)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST)
                .feature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS)
                .feature(EdenFeatures.TALL_COPPER_GRASS)
                .feature(EdenFeatures.TALL_IRON_GRASS)
                .feature(EdenFeatures.TALL_GOLD_GRASS)
                .feature(EdenFeatures.COPPER_GRASS)
                .feature(EdenFeatures.IRON_GRASS)
                .feature(EdenFeatures.GOLD_GRASS)
                .feature(EdenFeatures.BRAIN_TREE)
                .feature(EdenFeatures.LAYERED_IRON)
                .feature(EdenFeatures.LAYERED_COPPER)
                .feature(EdenFeatures.LAYERED_GOLD);
    }
}
