package paulevs.edenring.world.generator;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.util.MHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IslandLayer {
	private final Map<BlockPos, SDF> islandCache = Maps.newHashMap();
	private final List<BlockPos> positions = new ArrayList<>(9);
	private final List<SDF> nearIslands = new ArrayList<>(9);
	private final Random random = new Random();
	private final OpenSimplexNoise density;
	private final LayerOptions options;
	private final SDF island;
	private final int seed;
	private int lastX;
	private int lastZ;
	
	public IslandLayer(int seed, LayerOptions options) {
		this.density = new OpenSimplexNoise(seed);
		this.options = options;
		this.seed = seed;
		
		SDF cone1 = makeCone(0, 0.4F, 0.2F, -0.3F);
		SDF cone2 = makeCone(0.4F, 0.5F, 0.1F, -0.1F);
		SDF cone3 = makeCone(0.5F, 0.45F, 0.03F, 0.0F);
		SDF cone4 = makeCone(0.45F, 0, 0.02F, 0.03F);
		
		SDF coneBottom = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone1).setSourceB(cone2);
		SDF coneTop = new SDFSmoothUnion().setRadius(0.02F).setSourceA(cone3).setSourceB(cone4);
		island = new SDFSmoothUnion().setRadius(0.01F).setSourceA(coneTop).setSourceB(coneBottom);
	}
	
	public void updatePositions(double x, double z) {
		int ix = MHelper.floor(x / options.distance);
		int iz = MHelper.floor(z / options.distance);
		
		if (lastX != ix || lastZ != iz || positions.isEmpty()) {
			lastX = ix;
			lastZ = iz;
			nearIslands.clear();
			positions.clear();
			for (int pox = -1; pox < 2; pox++) {
				int px = pox + ix;
				for (int poz = -1; poz < 2; poz++) {
					int pz = poz + iz;
					random.setSeed(MHelper.getSeed(seed, px, pz));
					double posX = (px + random.nextFloat()) * options.distance;
					double posY = MHelper.randRange(options.minY, options.maxY, random);
					double posZ = (pz + random.nextFloat()) * options.distance;
					if (density.eval(posX * 0.01, posZ * 0.01) > options.coverage) {
						BlockPos pos = new BlockPos(posX, posY, posZ);
						nearIslands.add(getIsland(pos));
						positions.add(pos);
					}
				}
			}
		}
	}
	
	private SDF getIsland(BlockPos pos) {
		return islandCache.computeIfAbsent(pos, i -> {
			random.setSeed(MHelper.getSeed(seed, pos.getX(), pos.getZ()));
			return makeSimpleIsland(random);
		});
	}
	
	private SDF makeSimpleIsland(Random random) {
		final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextInt());
		final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextInt());
		final float scale1 = options.scale * 0.1F * 0.125F;
		final float scale2 = options.scale * 0.2F * 0.125F;
		final float scale3 = options.scale * 0.4F * 0.125F;
		
		final float scale6 = options.scale * 0.2F * 0.125F;
		final float scale7 = options.scale * 0.4F * 0.125F;
		
		final float scale5 = 1.0F / options.scale;//2.5F / options.scale;
		
		SDF island = new SDFScale().setScale(random.nextFloat() + 0.5F).setSource(this.island);
		island = new SDFCoordModify().setFunction(pos -> {
			float x1 = pos.x() * scale1;
			float z1 = pos.z() * scale1;
			float x2 = pos.x() * scale2;
			float z2 = pos.z() * scale2;
			//float x3 = pos.x() * scale3;
			//float z3 = pos.z() * scale3;
			
			float x6 = pos.x() * scale6;
			float z6 = pos.z() * scale6;
			float x7 = pos.x() * scale7;
			float z7 = pos.z() * scale7;
			
			//float x4 = pos.x() * scale4;
			//float z4 = pos.z() * scale4;
			float dx = (float) noise1.eval(x1, z1) * 20 + (float) noise2.eval(x2, z2) * 10;// + (float) noise1.eval(x3, z3) * 5;
			float dy = (float) noise1.eval(x6, z6) *  6 + (float) noise2.eval(x7, z7) *  3;// + (float) noise1.eval(x4, z4) * 1.5F;
			float dz = (float) noise2.eval(x1, z1) * 20 + (float) noise1.eval(x2, z2) * 10;// + (float) noise2.eval(x3, z3) * 5;
			pos.set(pos.x() + dx * scale5, pos.y() + dy * scale5, pos.z() + dz * scale5);
		}).setSource(island);
		return island;
	}
	
	private float getRelativeDistance(SDF sdf, BlockPos center, double px, double py, double pz) {
		float x = (float) (px - center.getX()) / options.scale;
		float y = (float) (py - center.getY()) / options.scale;
		float z = (float) (pz - center.getZ()) / options.scale;
		return sdf.getDistance(x, y, z);
	}
	
	private float calculateSDF(double x, double y, double z) {
		float distance = 10;
		final byte size = (byte) positions.size();
		for (byte i = 0; i < size; i++) {
			SDF island = nearIslands.get(i);
			BlockPos pos = positions.get(i);
			float dist = getRelativeDistance(island, pos, x, y, z);
			if (dist < distance) {
				distance = dist;
			}
			if (distance < 0) {
				break;
			}
		}
		return distance;
	}
	
	public float getDensity(double x, double y, double z) {
		return -calculateSDF(x, y, z);
	}
	
	public void clearCache() {
		if (islandCache.size() > 128) {
			islandCache.clear();
		}
	}
	
	private static SDF makeCone(float radiusBottom, float radiusTop, float height, float minY) {
		float hh = height * 0.5F;
		SDF sdf = new SDFCappedCone().setHeight(hh).setRadius1(radiusBottom).setRadius2(radiusTop);
		return new SDFTranslate().setTranslate(0, minY + hh, 0).setSource(sdf);
	}
}
