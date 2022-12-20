package paulevs.edenring.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.betterx.bclib.util.MHelper;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public final class MoonInfo {
	public final float orbitRadius;
	public final float orbitState;
	public final float orbitAngle;
	public final Vector3f color;
	public final float speed;
	public final float size;
	
	public MoonInfo(RandomSource random) {
		orbitState = random.nextFloat() * (float) Math.PI * 2;
		orbitRadius = MHelper.randRange(10F, 30F, random);
		orbitAngle = MHelper.randRange(-30F, 30F, random);
		speed = MHelper.randRange(2F, 6F, random);
		size = MHelper.randRange(0.5F, 1.5F, random);
		
		float r = MHelper.randRange(0.7F, 1F, random);
		float b = MHelper.randRange(0.7F, 1F, random);
		float g = MHelper.min(r, b) + MHelper.randRange(0.01F, 0.05F, random);
		g = Mth.clamp(g, 0, 1);
		color = new Vector3f(r, g, b);
	}
}
