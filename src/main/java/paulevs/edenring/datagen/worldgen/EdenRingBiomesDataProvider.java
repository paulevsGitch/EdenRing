package paulevs.edenring.datagen.worldgen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v3.datagen.TagDataProvider;
import org.betterx.worlds.together.tag.v3.TagManager;
import paulevs.edenring.EdenRing;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.world.biome.EdenRingBiome;
import paulevs.edenring.world.biome.air.AirOceanBiome;
import paulevs.edenring.world.biome.air.SkyColonyBiome;
import paulevs.edenring.world.biome.cave.EmptyCaveBiome;
import paulevs.edenring.world.biome.cave.ErodedCaveBiome;
import paulevs.edenring.world.biome.land.*;
import paulevs.edenring.world.biome.land.OldMycoticForestBiome;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EdenRingBiomesDataProvider extends TagDataProvider<Biome> {
    public static final List<EdenRingBiome> BIOMES_LAND = Lists.newArrayList();
    public static final List<EdenRingBiome> BIOMES_AIR = Lists.newArrayList();
    public static final List<EdenRingBiome> BIOMES_CAVE = Lists.newArrayList();

    // LAND //
    private static final EdenRingBiome STONE_GARDEN = registerLand(new StoneGardenBiome());
    private static final EdenRingBiome GOLDEN_FOREST = registerLand(new GoldenForestBiome());
    private static final EdenRingBiome MYCOTIC_FOREST = registerLand(new MycoticForestBiome());
    private static final EdenRingBiome PULSE_FOREST = registerLand(new PulseForestBiome());
    private static final EdenRingBiome BRAINSTORM = registerLand(new BrainstormBiome());
    private static final EdenRingBiome LAKESIDE_DESERT = registerLand(new LakesideDesertBiome());
    private static final EdenRingBiome WIND_VALLEY = registerLand(new WindValleyBiome());

    // AIR //
    private static final EdenRingBiome AIR_OCEAN = registerVoid(new AirOceanBiome());
    private static final EdenRingBiome SKY_COLONY = registerVoid(new SkyColonyBiome());

    // CAVES //
    private static final EdenRingBiome EMPTY_CAVE = registerCave(new EmptyCaveBiome());
    private static final EdenRingBiome ERODED_CAVE = registerCave(new ErodedCaveBiome());

    // SUBBIOMES //
    private static final EdenRingBiome OLD_MYCOTIC_FOREST = registerSubBiome(new OldMycoticForestBiome(), MYCOTIC_FOREST);

    public EdenRingBiomesDataProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(TagManager.BIOMES, List.of(EdenRing.MOD_ID), output, registriesFuture);
    }

    public static void bootstrap(BootstapContext<Biome> ctx) {
        BCLBiomeBuilder.registerUnbound(ctx);
    }

    public static void ensureStaticallyLoaded() {
    }

    private static EdenRingBiome registerBiome(EdenRingBiome.Config biomeConfig, BiomeAPI.BiomeType type) {
        return EdenRingBiome.create(biomeConfig, type);
    }

    private static EdenRingBiome registerLand(EdenRingBiome.Config biomeConfig) {
        var biome = registerBiome(biomeConfig, EdenBiomes.EDEN_LAND);
        BIOMES_LAND.add(biome);
        return biome;
    }

    private static EdenRingBiome registerVoid(EdenRingBiome.Config biomeConfig) {
        var biome = registerBiome(biomeConfig, EdenBiomes.EDEN_LAND);
        BIOMES_AIR.add(biome);
        return biome;
    }

    private static EdenRingBiome registerCave(EdenRingBiome.Config biomeConfig) {
        var biome = registerBiome(biomeConfig, EdenBiomes.EDEN_LAND);
        BIOMES_CAVE.add(biome);
        return biome;
    }

    private static EdenRingBiome registerSubBiome(EdenRingBiome.Config biomeConfig, EdenRingBiome parent) {
        var biome = EdenRingBiome.createSubBiome(biomeConfig, parent);
        var type = parent.getIntendedType();
        if (type == EdenBiomes.EDEN_LAND) {
            BIOMES_LAND.add(biome);
        } else if (type == EdenBiomes.EDEN_VOID) {
            BIOMES_AIR.add(biome);
        } else if (type == EdenBiomes.EDEN_CAVE) {
            BIOMES_CAVE.add(biome);
        }
        return biome;
    }
}
