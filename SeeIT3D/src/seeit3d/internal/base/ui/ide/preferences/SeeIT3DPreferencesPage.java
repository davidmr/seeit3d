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
package seeit3d.internal.base.ui.ide.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import seeit3d.internal.Activator;
import seeit3d.internal.SeeIT3D;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.visual.api.IColorScaleRegistry;
import seeit3d.internal.base.visual.colorscale.IColorScale;

import com.google.inject.Inject;

/**
 * This class is in charge of showing the preferences page of SeeIT3D in Eclipse global preferences
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private IColorScaleRegistry colorScaleRegistry;

	public SeeIT3DPreferencesPage() {
		super(GRID);
		SeeIT3D.injector().injectMembers(this);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Note: Background Color and Color Scale by Default requires restart. Other colors need visualization refresh to take effect.");
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(ISeeIT3DPreferences.CONTAINERS_PER_ROW, "Containers per row", getFieldEditorParent()));
		addField(new IntegerFieldEditor(ISeeIT3DPreferences.POLYCYLINDERS_PER_ROW, "Polycylinders per row", getFieldEditorParent()));
		addField(new ColorFieldEditor(ISeeIT3DPreferences.BACKGROUND_COLOR, "Background Color (*)", getFieldEditorParent()));
		addField(new ColorFieldEditor(ISeeIT3DPreferences.HIGHLIGHT_COLOR, "Highlight Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(ISeeIT3DPreferences.RELATION_COLOR, "Relation Mark Color", getFieldEditorParent()));
		addField(new IntegerFieldEditor(ISeeIT3DPreferences.SCALE_STEP, "Scale Step (%)", getFieldEditorParent(), 2));
		addField(new IntegerFieldEditor(ISeeIT3DPreferences.TRANSPARENCY_STEP, "Transparency Step (%)", getFieldEditorParent(), 2));

		Iterable<IColorScale> allColorScales = colorScaleRegistry.allColorScales();

		List<String> colorScaleNames = new ArrayList<String>();

		for (IColorScale colorScale : allColorScales) {
			colorScaleNames.add(colorScale.getName());
		}

		String[][] colorScalesNames = new String[colorScaleNames.size()][2];
		for (int i = 0; i < colorScalesNames.length; i++) {
			colorScalesNames[i][0] = colorScaleNames.get(i);
			colorScalesNames[i][1] = colorScaleNames.get(i);
		}

		addField(new ComboFieldEditor(ISeeIT3DPreferences.COLOR_SCALE, "Color scale by default", colorScalesNames, getFieldEditorParent()));

	}

	@Inject
	public void setColorScaleRegistry(IColorScaleRegistry colorScaleRegistry) {
		this.colorScaleRegistry = colorScaleRegistry;
	}

}
