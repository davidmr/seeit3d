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

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import seeit3d.Activator;
import seeit3d.colorscale.ColorScaleRegistry;
import seeit3d.colorscale.IColorScale;

/**
 * This class is in charge of showing the preferences page of SeeIT3D in Eclipse global preferences
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public SeeIT3DPreferencesPage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Note: In order to color changes to take effect, the visualization view must be reset");
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(Preferences.CONTAINERS_PER_ROW, "Containers per row", getFieldEditorParent()));
		addField(new IntegerFieldEditor(Preferences.POLYCYLINDERS_PER_ROW, "Polycylinders per row", getFieldEditorParent()));
		addField(new ColorFieldEditor(Preferences.BACKGROUND_COLOR, "Background Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(Preferences.HIGHLIGHT_COLOR, "Highlight Color", getFieldEditorParent()));
		addField(new ColorFieldEditor(Preferences.RELATION_COLOR, "Relation Mark Color", getFieldEditorParent()));
		addField(new IntegerFieldEditor(Preferences.SCALE_STEP, "Scale Step (%)", getFieldEditorParent(), 2));
		addField(new IntegerFieldEditor(Preferences.TRANSPARENCY_STEP, "Transparency Step (%)", getFieldEditorParent(), 2));
		Iterable<IColorScale> allColorScales = ColorScaleRegistry.getInstance().allColorScales();

		List<String> colorScaleNames = new ArrayList<String>();

		for (IColorScale colorScale : allColorScales) {
			colorScaleNames.add(colorScale.getName());
		}

		String[][] colorScalesNames = new String[colorScaleNames.size()][2];
		for (int i = 0; i < colorScalesNames.length; i++) {
			colorScalesNames[i][0] = colorScaleNames.get(i);
			colorScalesNames[i][1] = colorScaleNames.get(i);
		}

		addField(new ComboFieldEditor(Preferences.COLOR_SCALE, "Color scale by default", colorScalesNames, getFieldEditorParent()));

	}

}
