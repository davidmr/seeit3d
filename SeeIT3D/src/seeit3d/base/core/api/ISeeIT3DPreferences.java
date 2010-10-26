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
package seeit3d.base.core.api;

import javax.vecmath.Color3f;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import seeit3d.base.visual.colorscale.IColorScale;

/**
 * Interface that defines the actions of the preferences included in SeeIT 3D
 * 
 * @author David Montaño
 * 
 */
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
