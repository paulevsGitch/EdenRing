package paulevs.edenring.blocks.complex;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import paulevs.edenring.EdenRing;

public class BrainTreeComplexMaterial extends WoodenComplexMaterial {
	public BrainTreeComplexMaterial(String baseName) {
		super(EdenRing.MOD_ID, baseName, "eden", MapColor.COLOR_LIGHT_GRAY, MapColor.COLOR_LIGHT_GRAY);
	}
	
	@Override
	protected FabricBlockSettings getBlockSettings() {
		return FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).sounds(SoundType.NETHERITE_BLOCK).mapColor(planksColor);
	}
}
