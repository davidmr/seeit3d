/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.preferences;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.RGB;

import seeit3d.colorscale.ColorScaleFactory;
import seeit3d.colorscale.IColorScale;
import seeit3d.colorscale.imp.ColdToHotColorScale;

/**
 * SeeIT3D preferences representation. It holds the values of each preference
 * 
 * @author David Montaño
 * 
 */
public class Preferences implements IPropertyChangeListener {

	public static final String CONTAINERS_PER_ROW = "containersPerRow";

	public static final String POLYCYLINDERS_PER_ROW = "polycylindersPerRow";

	public static final String BACKGROUND_COLOR = "backgroundColor";

	public static final String HIGHLIGHT_COLOR = "highlightColor";

	public static final String RELATION_COLOR = "relationColor";

	public static final String SCALE_STEP = "scaleStep";

	public static final String TRANSPARENCY_STEP = "transparencyStep";

	public static final String COLOR_SCALE = "colorScale";

	private static final Preferences instance;

	static {
		instance = new Preferences();
	}

	public static synchronized Preferences getInstance() {
		return instance;
	}

	private final List<IPreferencesListener> listeners;

	private Color3f backgroundColor;

	private int containersPerRow;

	private double scaleStep;

	private int polycylindersPerRow;

	private Color3f highlightColor;

	private Color3f relationMarkColor;

	private float transparencyStep;

	private IColorScale colorScale;

	public Preferences() {
		listeners = new ArrayList<IPreferencesListener>();
	}

	public void registerListener(IPreferencesListener listener) {
		listeners.add(listener);
	}

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
		} else if (property.equals(COLOR_SCALE)) {
			String colorScaleName = (String) event.getNewValue();
			updateColorScale(colorScaleName);
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

	public IColorScale getColorScale() {
		return colorScale;
	}

	public void setPreferencesDefaults(IPreferenceStore preferenceStore) {
		preferenceStore.setDefault(CONTAINERS_PER_ROW, 3);
		preferenceStore.setDefault(POLYCYLINDERS_PER_ROW, 20);
		preferenceStore.setDefault(BACKGROUND_COLOR, "255,255,238");
		preferenceStore.setDefault(HIGHLIGHT_COLOR, "0,255,0");
		preferenceStore.setDefault(RELATION_COLOR, "255,255,0");
		preferenceStore.setDefault(SCALE_STEP, 20);
		preferenceStore.setDefault(TRANSPARENCY_STEP, 10);
		preferenceStore.setDefault(COLOR_SCALE, new ColdToHotColorScale().getName());
	}

	public void loadStoredPreferences(IPreferenceStore preferenceStore) {
		int containersPerRow = preferenceStore.getInt(CONTAINERS_PER_ROW);
		int polycylindersPerRow = preferenceStore.getInt(POLYCYLINDERS_PER_ROW);
		String backgroundColor = preferenceStore.getString(BACKGROUND_COLOR);
		String highlightColor = preferenceStore.getString(HIGHLIGHT_COLOR);
		String relationColor = preferenceStore.getString(RELATION_COLOR);
		int scaleStep = preferenceStore.getInt(SCALE_STEP);
		int transparencyStep = preferenceStore.getInt(TRANSPARENCY_STEP);
		String colorScaleName = preferenceStore.getString(COLOR_SCALE);

		updateContainersPerRow(containersPerRow);
		updatePolycylindersPerRow(polycylindersPerRow);
		updateBackgroundColor(backgroundColor);
		updateHighlightColor(highlightColor);
		updateRelationColor(relationColor);
		updateScaleStep(scaleStep);
		updateTransparencyStep(transparencyStep);
		updateColorScale(colorScaleName);

	}

	private void updateContainersPerRow(int containersPerRow) {
		this.containersPerRow = containersPerRow;
		for (IPreferencesListener listener : listeners) {
			listener.containersPerRowChanged(containersPerRow);
		}
	}

	private void updatePolycylindersPerRow(int polycylindersPerRow) {
		this.polycylindersPerRow = polycylindersPerRow;
		for (IPreferencesListener listener : listeners) {
			listener.polycylindersPerRowChanged(polycylindersPerRow);
		}

	}

	private void updateBackgroundColor(String backgroundColor) {
		Color3f color = extractColorFromString(backgroundColor);
		this.backgroundColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.backgroundColorChanged(this.backgroundColor);
		}
	}

	private void updateBackgroundColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.backgroundColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.backgroundColorChanged(this.backgroundColor);
		}
	}

	private void updateHighlightColor(String highlightColor) {
		Color3f color = extractColorFromString(highlightColor);
		this.highlightColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.highlightColorChanged(this.highlightColor);
		}
	}

	private void updateHighlightColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.highlightColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.highlightColorChanged(this.highlightColor);
		}
	}

	private void updateRelationColor(String relationColor) {
		Color3f color = extractColorFromString(relationColor);
		this.relationMarkColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.relationMarkColorChanged(relationMarkColor);
		}
	}

	private void updateRelationColor(RGB newColor) {
		Color3f color = extractColorFromRGB(newColor);
		this.relationMarkColor = color;
		for (IPreferencesListener listener : listeners) {
			listener.relationMarkColorChanged(relationMarkColor);
		}
	}

	private void updateScaleStep(int scaleStep) {
		this.scaleStep = scaleStep / 100f;
		for (IPreferencesListener listener : listeners) {
			listener.scaleStepChanged(this.scaleStep);
		}
	}

	private void updateTransparencyStep(int transparencyStep) {
		this.transparencyStep = transparencyStep / 100f;
		for (IPreferencesListener listener : listeners) {
			listener.transparencyStepChanged(this.transparencyStep);
		}
	}

	private void updateColorScale(String colorScale) {
		IColorScale scale = ColorScaleFactory.findByName(colorScale);
		this.colorScale = scale;
		for (IPreferencesListener listener : listeners) {
			listener.colorScaleChanged(scale);
		}
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
