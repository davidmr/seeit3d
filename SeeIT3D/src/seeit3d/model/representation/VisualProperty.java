package seeit3d.model.representation;

/**
 * Defines the properties that characterizes a visual object
 * 
 * @author David
 * 
 */
public enum VisualProperty {

	COLOR("Color", 0.0f, 0.0f), HEIGHT("Height", 0.0f, 0.0f), CROSS_SECTION("Cross Section", 0.2f, 0.1f);

	private final String formattedName;

	/**
	 * From 0 to 1
	 */
	private final float defaultValue;

	/**
	 * From 0 to 1
	 */
	private final float minValue;

	private VisualProperty(String formattedName, float defaultValue, float minValue) {
		this.formattedName = formattedName;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
	}

	public float getDefaultValue() {
		return defaultValue;
	}

	public float getMinValue() {
		return minValue;
	}

	@Override
	public String toString() {
		return formattedName;
	}
}
