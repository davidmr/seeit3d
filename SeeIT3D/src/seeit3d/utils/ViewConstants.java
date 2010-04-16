package seeit3d.utils;

import javax.vecmath.Color3f;

/**
 * Constants in the view (Colors, and sizes)
 * 
 * @author David
 * 
 */
public class ViewConstants {

	public static final String VISUALIZATION_EXTENSION = "s3d";

	public static final Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);

	public static final Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);

	public static final Color3f RED = new Color3f(1.0f, 0.0f, 0.0f);

	public static final Color3f GREEN = new Color3f(0.0f, 1.0f, 0.0f);

	public static final Color3f BLUE = new Color3f(0.0f, 0.0f, 1.0f);

	public static final float RELATION_MARK_PADDING = 0.1f;

	public static final float RELATION_MARK_SCALING = 1.2f;

	public static final float PICKING_TOLERANCE = 0.0f;

	public static final float POLYCYLINDER_SPACING = 0.15f;

	public static final float CONTAINERS_SPACING = 0.5f;

	public static final float HIGHLIGHT_PADDING = 0.1f;

	public static final Color3f[] colors216 = new Color3f[218];

	static {

		colors216[0] = WHITE;
		int i = 1;
		for (float red = 0; red <= 255; red += 51) {
			for (float green = 0; green <= 255; green += 51) {
				for (float blue = 0; blue <= 255; blue += 51) {
					Color3f newColor = new Color3f(red / 256.0f, green / 256.0f, blue / 256.0f);
					colors216[i] = newColor;
					i++;
				}
			}
		}
		colors216[colors216.length - 1] = BLACK;

	}

}
