package seeit3d.base.core.api;

import javax.vecmath.Color3f;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import seeit3d.base.visual.colorscale.IColorScale;

public interface ISeeIT3DPreferences extends IPropertyChangeListener {

	String CONTAINERS_PER_ROW = "containersPerRow";

	String POLYCYLINDERS_PER_ROW = "polycylindersPerRow";

	String BACKGROUND_COLOR = "backgroundColor";

	String HIGHLIGHT_COLOR = "highlightColor";

	String RELATION_COLOR = "relationColor";

	String SCALE_STEP = "scaleStep";

	String TRANSPARENCY_STEP = "transparencyStep";

	String COLOR_SCALE = "colorScale";

	int getContainersPerRow();

	double getScaleStep();

	Color3f getBackgroundColor();

	void setPreferencesDefaults(IPreferenceStore preferenceStore);

	void loadStoredPreferences(IPreferenceStore preferenceStore);

	IColorScale getColorScale();

	float getTransparencyStep();

	Color3f getHighlightColor();

	int getPolycylindersPerRow();

	Color3f getRelationMarkColor();

}
