package seeit3d.general;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.model.Preferences;
import seeit3d.feedback.api.SeeIT3DFeedback;
import seeit3d.modelers.api.SeeIT3DModelers;
import seeit3d.ui.api.SeeIT3DUI;
import seeit3d.visual.api.DefaultSeeIT3DVisualProperties;
import seeit3d.visual.api.SeeIT3DVisualProperties;

public class SeeIT3DAPILocator {
	
	private static final SeeIT3DCore core;
	
	private static final SeeIT3DVisualProperties visual;

	static{
		core = new SeeIT3DManager();
		visual = new DefaultSeeIT3DVisualProperties();
	}

	public static SeeIT3DCore findCore() {
		return core;
	}

	public static SeeIT3DFeedback findFeedback() {
		return null;
	}

	public static SeeIT3DModelers findModelers() {
		return null;
	}

	public static SeeIT3DUI findUI() {
		return null;
	}

	public static SeeIT3DVisualProperties findVisualProperties() {
		return visual;
	}

	public static Preferences findPreferences() {
		return Preferences.getInstance();
	}
}
