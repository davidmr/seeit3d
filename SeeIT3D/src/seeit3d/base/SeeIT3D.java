package seeit3d.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.progress.UIJob;

import seeit3d.Activator;
import seeit3d.base.core.api.ISeeIT3DPreferences;
import seeit3d.base.error.ErrorHandler;
import seeit3d.base.ui.ide.commands.ChangeSortingPolyCylindersCriteriaCommand;
import seeit3d.base.ui.ide.observers.WorkspaceClosedObserver;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SeeIT3D {

	private static final List<ISeeIT3DContributor> contributions = new ArrayList<ISeeIT3DContributor>();

	private static Injector injector;

	public static void initialize() {
		try {
			injector = Guice.createInjector(new SeeIT3DModule());
			initializePreferences();
			initializeSortRadio();
			registerGlobalListener();
			ErrorHandler.setShell(new Shell(Display.getDefault()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initializePreferences() {
		ISeeIT3DPreferences preferences = injector.getInstance(ISeeIT3DPreferences.class);
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		preferences.setPreferencesDefaults(preferenceStore);
		preferences.loadStoredPreferences(preferenceStore);
		preferenceStore.addPropertyChangeListener(preferences);
	}

	private static void registerGlobalListener() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new WorkspaceClosedObserver());

	}

	private static void initializeSortRadio() {
		UIJob job = new UIJob("InitSortRadioState") {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {

				ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
				Command command = commandService.getCommand(ChangeSortingPolyCylindersCriteriaCommand.CHANGE_SORT_COMMAND_ID);
				command.isEnabled();
				return new Status(IStatus.OK, Activator.PLUGIN_ID, "Init commands workaround performed succesfully");
			}

		};
		job.schedule();
	}

	public static void contribute(ISeeIT3DContributor contributor) {
		contributor.initialize();
		contributions.add(contributor);
	}

	public static Injector injector() {
		checkInitialized();
		return injector;
	}

	private static void checkInitialized() {
		if (injector == null) {
			throw new IllegalStateException("SeeIT 3D not initialized");
		}
	}
}
