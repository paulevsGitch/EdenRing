package paulevs.edenring.registries;

import net.minecraft.world.item.Items;
import paulevs.edenring.EdenRing;
import ru.bclib.api.TagAPI;
import ru.bclib.config.PathConfig;
import ru.bclib.recipes.GridRecipe;

public class EdenRecipes {
	public static final PathConfig CONFIG = new PathConfig(EdenRing.MOD_ID, "recipes");
	
	public static void init() {
		GridRecipe.make(EdenRing.makeID("gravilite_block"), EdenBlocks.GRAVILITE).checkConfig(CONFIG).setShape("##", "##").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_shards"), EdenBlocks.GRAVILITE_SHARDS).checkConfig(CONFIG).setList("#").addMaterial('#', EdenBlocks.GRAVILITE).setOutputCount(4).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lamp"), EdenBlocks.GRAVILITE_LAMP).checkConfig(CONFIG).setShape(" I ", "I#I", " I ").addMaterial('#', EdenBlocks.GRAVILITE).addMaterial('I', TagAPI.ITEM_IRON_INGOTS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lantern_tall"), EdenBlocks.GRAVILITE_LANTERN_TALL).checkConfig(CONFIG).setShape("I", "#", "I").addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', TagAPI.ITEM_IRON_INGOTS).build();
		GridRecipe.make(EdenRing.makeID("gravilite_lantern"), EdenBlocks.GRAVILITE_LANTERN).checkConfig(CONFIG).setShape(" I ", "I#I", " I ").setOutputCount(2).addMaterial('#', EdenBlocks.GRAVILITE_SHARDS).addMaterial('I', TagAPI.ITEM_IRON_INGOTS).build();
		CONFIG.saveChanges();
	}
}
