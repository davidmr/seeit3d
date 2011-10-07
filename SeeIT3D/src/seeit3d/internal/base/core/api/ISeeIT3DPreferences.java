/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.core.api;

import javax.vecmath.Color3f;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import seeit3d.internal.base.visual.colorscale.IColorScale;

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
