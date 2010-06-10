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
package seeit3d;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.Preferences;
import seeit3d.general.model.generator.metrics.MetricsRegistry;
import seeit3d.general.model.utils.NoOpMetricCalculator;
import seeit3d.modelers.java.generator.metrics.*;
import seeit3d.ui.ide.commands.ChangeSortingPolyCylindersCriteriaCommand;
import seeit3d.ui.ide.observers.WorkspaceClosedObserver;

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
		initializePreferences();
		initializeSortRadio();
		registerGlobalListener();
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
