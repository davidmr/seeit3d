package seeit3d.general;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.core.handler.SeeIT3DCoreHandler;
import seeit3d.general.model.Preferences;
import seeit3d.ui.handler.SeeIT3DUIHandler;
import seeit3d.visual.api.SeeIT3DVisualProperties;
import seeit3d.visual.handler.SeeIT3DVisualPropertiesHandler;

public class SeeIT3DAPILocator {

	private static final SeeIT3DCore core;

	private static final SeeIT3DVisualProperties visual;

	private static final SeeIT3DUIHandler ui;

	static {
		core = new SeeIT3DCoreHandler();
		visual = new SeeIT3DVisualPropertiesHandler();
		ui = new SeeIT3DUIHandler();
	}

	public static SeeIT3DCore findCore() {
		return core;
	}

	public static SeeIT3DVisualProperties findVisualProperties() {
		return visual;
	}

	public static SeeIT3DUIHandler findUi() {
		return ui;
	}

	public static Preferences findPreferences() {
		return Preferences.getInstance();
	}
}
