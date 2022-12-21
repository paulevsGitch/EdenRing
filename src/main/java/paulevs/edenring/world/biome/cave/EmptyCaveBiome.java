package paulevs.edenring.world.biome.cave;

import net.minecraft.world.level.block.state.BlockState;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.surface.SurfaceRuleBuilder;
import org.betterx.bclib.interfaces.SurfaceMaterialProvider;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.world.biome.BiomesCommonMethods;
import paulevs.edenring.world.biome.EdenRingBiome;

public class EmptyCaveBiome extends EdenRingBiome.Config {
    public EmptyCaveBiome() {
        super(EdenBiomes.EMPTY_CAVE.location());
    }

    @Override
    protected void addCustomBuildData(BCLBiomeBuilder builder) {
        BiomesCommonMethods.addDefaultLandFeatures(builder);
        builder.plantsColor(0x707c47);
    }

    @Override
    protected SurfaceMaterialProvider surfaceMaterial() {
        return new EdenRingBiome.DefaultSurfaceMaterialProvider() {

            @Override
            public BlockState getTopMaterial() {
                return STONE;
            }

            @Override
            public BlockState getUnderMaterial() {
                return STONE;
            }

            @Override
            public int subSurfaceDepth() {
                return 0;
            }
        };
    }
}
