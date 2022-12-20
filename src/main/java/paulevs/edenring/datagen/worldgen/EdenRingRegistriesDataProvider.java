package paulevs.edenring.datagen.worldgen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import org.betterx.bclib.api.v3.datagen.RegistriesDataProvider;
import org.betterx.bclib.api.v3.datagen.RegistrySupplier;
import org.betterx.worlds.together.util.Logger;
import paulevs.edenring.EdenRing;
import paulevs.edenring.datagen.EdenRingRegistrySupplier;

import java.util.concurrent.CompletableFuture;

public class EdenRingRegistriesDataProvider extends RegistriesDataProvider {
    public EdenRingRegistriesDataProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture
    ) {
        super(EdenRing.LOGGER, EdenRingRegistrySupplier.INSTANCE, output, registriesFuture);
    }
}
