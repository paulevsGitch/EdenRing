package paulevs.edenring.paintings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import paulevs.edenring.EdenRing;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EdenPaintings {
	private static final Map<ResourceLocation, PaintingInfo> PAINTING_BY_ID = Maps.newHashMap();
	private static final List<PaintingInfo> PAINTINGS_LIST = Lists.newArrayList();
	
	public static void init() {
		PaintingColorProvider biomeColor = (level, pos) -> level.calculateBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER);
		register("limphium_leaves_small_1", 16, 16, biomeColor);
		register("limphium_leaves_small_2", 16, 16, biomeColor);
		register("limphium_leaves_small_3", 16, 16, biomeColor);
		register("limphium_leaves_small_4", 16, 16, biomeColor);
		register("limphium_leaves_small_5", 16, 16, biomeColor);
		register("limphium_leaves_medium_1", 32, 32, biomeColor);
		register("limphium_leaves_medium_2", 16, 32, biomeColor);
		register("limphium_leaves_medium_3", 16, 32, biomeColor);
		register("limphium_leaves_medium_4", 32, 16, biomeColor);
		register("limphium_leaves_medium_5", 32, 16, biomeColor);
	}
	
	private static void register(String name, int width, int height) {
		register(name, width, height, null);
	}
	
	private static void register(String name, int width, int height, @Nullable PaintingColorProvider provider) {
		ResourceLocation id = EdenRing.makeID(name);
		ResourceLocation tex = EdenRing.makeID("textures/painting/" + name + ".png");
		PaintingInfo info = new PaintingInfo(PAINTINGS_LIST.size(), id, tex, width, height, provider);
		PAINTING_BY_ID.put(id, info);
		PAINTINGS_LIST.add(info);
	}
	
	public static PaintingInfo getPainting(ResourceLocation id) {
		return PAINTING_BY_ID.get(id);
	}
	
	public static PaintingInfo getPainting(int id) {
		return PAINTINGS_LIST.get(id);
	}
	
	public static Collection<PaintingInfo> getPaintings() {
		return PAINTING_BY_ID.values();
	}
}
