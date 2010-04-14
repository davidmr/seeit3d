package seeit3d.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import seeit3d.Activator;

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
	}

}
