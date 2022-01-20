package paulevs.edenring.mixin.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(HoeItem.class)
public interface HoeItemAccessor {
	@Accessor("TILLABLES")
	static Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> eden_getTillables() {
		throw new AssertionError("@Accessor dummy body called");
	}
	
	@Accessor("TILLABLES")
	static void eden_setTillables(Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> map) {
		throw new AssertionError("@Accessor dummy body called");
	}
}
