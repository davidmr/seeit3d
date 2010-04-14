package seeit3d.preferences;

import javax.vecmath.Color3f;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;

public class Preferences implements IPropertyChangeListener {

	public static final String CONTAINERS_PER_ROW = "containersPerRow";

	public static final String POLYCYLINDERS_PER_ROW = "polycylindersPerRow";

	public static final String BACKGROUND_COLOR = "backgroundColor";

	public static final String HIGHLIGHT_COLOR = "highlightColor";

	public static final String RELATION_COLOR = "relationColor";

	public static final String SCALE_STEP = "scaleStep";

	public static final String TRANSPARENCY_STEP = "transparencyStep";

	private static final Preferences instance;

	static {
		instance = new Preferences();
	}

	public static synchronized Preferences getInstance() {
		return instance;
	}

	private Color3f backgroundColor;

	private int containersPerRow;

	private double scaleStep;

	private int polycylindersPerRow;

	private Color3f highlightColor;

	private Color3f relationMarkColor;

	private float transparencyStep;

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (property.equals(CONTAINERS_PER_ROW)) {
			Integer newValue = (Integer) event.getNewValue();
			updateContainersPerRow(newValue);
		} else if (property.equals(POLYCYLINDERS_PER_ROW)) {
			Integer newValue = (Integer) event.getNewValue();
			updatePolycylindersPerRow(newValue);
		} else if (property.equals(BACKGROUND_COLOR)) {
			Object newValue = event.getNewValue();
			if (newValue instanceof RGB) {
				RGB backgroundColor = (RGB) newValue;
				updateBackgroundColor(backgroundColor);
			} else if (newValue instanceof String) {
				String backgroundColor = (String) newValue;
				updateBackgroundColor(backgroundColor);
			}
		} else if (property.equals(HIGHLIGHT_COLOR)) {
			Object newValue = event.getNewValue();
			if (newValue instanceof RGB) {
				RGB highlightColor = (RGB) newValue;
				updateHighlightColor(highlightColor);
			} else if (newValue instanceof String) {
				String highlightColor = (String) newValue;
				updateHighlightColor(highlightColor);
			}
		} else if (property.equals(RELATION_COLOR)) {
			Object newValue = event.getNewValue();
			if (newValue instanceof RGB) {
				RGB relationColor = (RGB) newValue;
				updateRelationColor(relationColor);
			} else if (newValue instanceof String) {
				String relationColor = (String) newValue;
				updateRelationColor(relationColor);
			}
		} else if (property.equals(SCALE_STEP)) {
			Integer newValue = (Integer) event.getNewValue();
			updateScaleStep(newValue);
		} else if (property.equals(TRANSPARENCY_STEP)) {
			Integer newValue = (Integer) event.getNewValue();
			updateTransparencyStep(newValue);
		}
	}

	public Color3f getBackgroundColor() {
		return backgroundColor;
	}

	public int getContainersPerRow() {
		return containersPerRow;
	}

	public double getScaleStep() {
		return scaleStep;
	}

	public int getPolycylindersPerRow() {
		return polycylindersPerRow;
	}

	public Color3f getHighlightColor() {
		return highlightColor;
	}

	public Color3f getRelationMarkColor() {
		return relationMarkColor;
	}

	public float getTransparencyStep() {
		return transparencyStep;
	}

	public void setPreferencesDefaults(IPreferenceStore preferenceStore) {
		preferenceStore.setDefault(Preferences.CONTAINERS_PER_ROW, 3);
		preferenceStore.setDefault(Preferences.POLYCYLINDERS_PER_ROW, 20);
		preferenceStore.setDefault(Preferences.BACKGROUND_COLOR, "255,255,238");
		preferenceStore.setDefault(Preferences.HIGHLIGHT_COLOR, "0,255,0");
		preferenceStore.setDefault(Preferences.RELATION_COLOR, "255,255,0");
		preferenceStore.setDefault(Preferences.SCALE_STEP, 20);
		preferenceStore.setDefault(Preferences.TRANSPARENCY_STEP, 10);
	}

	public void loadStoredPreferences(IPreferenceStore preferenceStore) {
		int containersPerRow = preferenceStore.getInt(Preferences.CONTAINERS_PER_ROW);
		int polycylindersPerRow = preferenceStore.getInt(Preferences.POLYCYLINDERS_PER_ROW);
		String backgroundColor = preferenceStore.getString(Preferences.BACKGROUND_COLOR);
		String highlightColor = preferenceStore.getString(Preferences.HIGHLIGHT_COLOR);
		String relationColor = preferenceStore.getString(Preferences.RELATION_COLOR);
		int scaleStep = preferenceStore.getInt(Preferences.SCALE_STEP);
		int transparencyStep = preferenceStore.getInt(Preferences.TRANSPARENCY_STEP);

		updateContainersPerRow(containersPerRow);
		updatePolycylindersPerRow(polycylindersPerRow);
		updateBackgroundColor(backgroundColor);
		updateHighlightColor(highlightColor);
		updateRelationColor(relationColor);
		updateScaleStep(scaleStep);
		updateTransparencyStep(transparencyStep);
	}

	private void updateContainersPerRow(int containersPerRow) {
		this.containersPerRow = containersPerRow;

	}

	private void updatePolycylindersPerRow(int polycylindersPerRow) {
		this.polycylindersPerRow = polycylindersPerRow;

	}

	private void updateBackgroundColor(String backgroundColor) {
		Color3f color = extractColorFromString(backgroundColor);
		this.backgroundColor = color;
	}

	private void updateBackgroundColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.backgroundColor = color;
	}

	private void updateHighlightColor(String highlightColor) {
		Color3f color = extractColorFromString(highlightColor);
		this.highlightColor = color;
	}

	private void updateHighlightColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.highlightColor = color;
	}

	private void updateRelationColor(String relationColor) {
		Color3f color = extractColorFromString(relationColor);
		this.relationMarkColor = color;
	}

	private void updateRelationColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.relationMarkColor = color;
	}

	private void updateScaleStep(int scaleStep) {
		this.scaleStep = scaleStep / 100f;

	}

	private void updateTransparencyStep(int transparencyStep) {
		this.transparencyStep = transparencyStep / 100f;
	}

	private Color3f extractColorFromString(String colorStr) {
		String[] colorRGB = colorStr.trim().split(",");
		float r = (new Float(colorRGB[0]) / 255f);
		float g = (new Float(colorRGB[1]) / 255f);
		float b = (new Float(colorRGB[2]) / 255f);
		return new Color3f(r, g, b);
	}

	private Color3f extractColorFromRGB(RGB color) {
		float r = (new Float(color.red) / 255f);
		float g = (new Float(color.green) / 255f);
		float b = (new Float(color.blue) / 255f);
		return new Color3f(r, g, b);
	}

}
