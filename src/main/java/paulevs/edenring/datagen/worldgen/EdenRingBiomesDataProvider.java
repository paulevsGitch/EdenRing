package paulevs.edenring.datagen.worldgen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.bclib.config.EntryConfig;
import org.betterx.bclib.config.IdConfig;
import org.betterx.worlds.together.tag.v3.TagManager;
import org.betterx.worlds.together.tag.v3.TagRegistry;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.biomes.CaveBiomes;
import paulevs.edenring.world.biomes.LandBiomes;
import paulevs.edenring.world.biomes.VoidBiomes;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EdenRingBiomesDataProvider extends TagDataProvider<Biome> {
    private static final IdConfig CONFIG = new EntryConfig(EdenRing.MOD_ID, "biomes");
    public static final List<BCLBiome> BIOMES_LAND = Lists.newArrayList();
    public static final List<BCLBiome> BIOMES_VOID = Lists.newArrayList();
    public static final List<BCLBiome> BIOMES_CAVE = Lists.newArrayList();

    // LAND //
    public static final BCLBiome STONE_GARDEN = registerLand(LandBiomes.makeStoneGardenBiome());
    public static final BCLBiome GOLDEN_FOREST = registerLand(LandBiomes.makeGoldenForestBiome());
    public static final BCLBiome MYCOTIC_FOREST = registerLand(LandBiomes.makeMycoticForestBiome());
    public static final BCLBiome PULSE_FOREST = registerLand(LandBiomes.makePulseForestBiome());
    public static final BCLBiome BRAINSTORM = registerLand(LandBiomes.makeBrainstormBiome());
    public static final BCLBiome LAKESIDE_DESERT = registerLand(LandBiomes.makeLakesideDesertBiome());
    public static final BCLBiome WIND_VALLEY = registerLand(LandBiomes.makeWindValleyBiome());

    // VOID //
    public static final BCLBiome AIR_OCEAN = registerVoid(VoidBiomes.makeAirOcean());
    public static final BCLBiome SKY_COLONY = registerVoid(VoidBiomes.makeSkyColony());

    // CAVES //
    public static final BCLBiome EMPTY_CAVE = registerCave(CaveBiomes.makeEmptyCaveBiome());
    public static final BCLBiome ERODED_CAVE = registerCave(CaveBiomes.makeErodedCaveBiome());

    // SUBBIOMES //
    public static final BCLBiome OLD_MYCOTIC_FOREST = registerSubLand(MYCOTIC_FOREST, LandBiomes.makeOldMycoticForestBiome());

    public EdenRingBiomesDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(TagManager.BIOMES, List.of(EdenRing.MOD_ID), output, registriesFuture);
    }

    public static void bootstrap(BootstapContext<Biome> ctx) {
        BCLBiomeBuilder.registerUnbound(ctx);
    }

    public static void ensureStaticallyLoaded() {
    }

    private static BCLBiome registerLand(BCLBiome biome) {
        BIOMES_LAND.add(biome);
        return biome;
    }

    private static BCLBiome registerVoid(BCLBiome biome) {
        BIOMES_VOID.add(biome);
        return biome;
    }

    private static BCLBiome registerCave(BCLBiome biome) {
        BIOMES_CAVE.add(biome);
        return biome;
    }

    private static BCLBiome registerSubLand(BCLBiome parent, BCLBiome biome) {
        parent.addSubBiome(biome);
        return biome;
    }
}
