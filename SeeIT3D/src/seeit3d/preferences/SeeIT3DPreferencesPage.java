package seeit3d.preferences;

import java.util.List;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import seeit3d.Activator;
import seeit3d.colorscale.ColorScaleFactory;
import seeit3d.colorscale.IColorScale;

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
		List<IColorScale> allColorScales = ColorScaleFactory.createAllColorScales();
		String[][] colorScalesNames = new String[allColorScales.size()][2];
		for (int i = 0; i < colorScalesNames.length; i++) {
			colorScalesNames[i][0] = allColorScales.get(i).getName();
			colorScalesNames[i][1] = allColorScales.get(i).getName();
		}

		addField(new ComboFieldEditor(Preferences.COLOR_SCALE, "Color scale by default", colorScalesNames, getFieldEditorParent()));

	}

}
