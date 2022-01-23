package paulevs.edenring.world.generator;

public class LandCache {
	private static final byte BITS = 6;
	private static final byte SIDE = 64;
	private static final byte MASK = SIDE - 1;
	private static final short SIZE = SIDE * SIDE;
	private final boolean[] data = new boolean[SIZE];
	
	public void setData(int x, int z, boolean value) {
		data[getIndex(x, z)] = value;
	}
	
	public boolean getData(int x, int z) {
		return data[getIndex(x, z)];
	}
	
	private int getIndex(int x, int z) {
		return x << BITS | z;
	}
	
	public static int wrap(int coordinate) {
		return coordinate & MASK;
	}
	
	public static int getSide() {
		return SIDE;
	}
	
	public static int toChunk(int coordinate) {
		return coordinate >> BITS;
	}
	
	public static int fromChunk(int coordinate) {
		return coordinate << BITS;
	}
}
