package paulevs.edenring.client;

public class ItemScaler {
	private static boolean rescale;
	private static float scale;
	
	public static boolean needRescale() {
		boolean rescaleItem = rescale;
		rescale = false;
		return rescaleItem;
	}
	
	public static void setScale(float scale) {
		rescale = true;
		ItemScaler.scale = scale / 16F;
	}
	
	public static float getScale() {
		return scale;
	}
}
