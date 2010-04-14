package seeit3d;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import seeit3d.commands.ChangeSortingPolyCylindersCriteriaCommand;
import seeit3d.error.ErrorHandler;
import seeit3d.metrics.MetricsRegistry;
import seeit3d.metrics.NoOpMetricCalculator;
import seeit3d.model.java.metrics.*;
import seeit3d.observers.ProjectSelectionObserver;
import seeit3d.observers.WorkspaceClosedObserver;
import seeit3d.preferences.Preferences;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "seeit3d";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		initializeSortRadio();
		registerGlobalListener();
		initializePreferences();
		registerSingletonMetrics();
		ErrorHandler.setShell(new Shell(Display.getDefault()));
	}

	private void initializePreferences() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		Preferences preferences = Preferences.getInstance();
		preferences.setPreferencesDefaults(preferenceStore);
		preferences.loadStoredPreferences(preferenceStore);
		preferenceStore.addPropertyChangeListener(preferences);
	}

	private void registerGlobalListener() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new WorkspaceClosedObserver());
		ISelectionService selection = (ISelectionService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISelectionService.class);
		selection.addSelectionListener(new ProjectSelectionObserver());
	}

	private void initializeSortRadio() {
		UIJob job = new UIJob("InitSortRadioState") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {

				ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
				Command command = commandService.getCommand(ChangeSortingPolyCylindersCriteriaCommand.CHANGE_SORT_COMMAND_ID);
				command.isEnabled();
				return new Status(IStatus.OK, PLUGIN_ID, "Init commands workaround performed succesfully");
			}

		};
		job.schedule();
	}

	private void registerSingletonMetrics() {
		MetricsRegistry registry = MetricsRegistry.getInstance();
		registry.registerMetric(new LOCCalculator());
		registry.registerMetric(new ControlStructureCalculator());
		registry.registerMetric(new ComplexityCalculator());
		registry.registerMetric(new NoOpMetricCalculator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
