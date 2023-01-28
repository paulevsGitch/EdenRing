package paulevs.edenring.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import paulevs.edenring.EdenRing;
import paulevs.edenring.datagen.worldgen.EdenRingBiomesDataProvider;
import paulevs.edenring.datagen.worldgen.EdenRingRegistriesDataProvider;

public class EdenRingDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        EdenRingBiomesDataProvider.ensureStaticallyLoaded();

        final FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(EdenRingBiomesDataProvider::new);
        pack.addProvider(EdenRingRegistriesDataProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        EdenRingRegistrySupplier.INSTANCE.bootstrapRegistries(registryBuilder);
        registryBuilder.add(Registries.BIOME, EdenRingBiomesDataProvider::bootstrap);
    }
}
