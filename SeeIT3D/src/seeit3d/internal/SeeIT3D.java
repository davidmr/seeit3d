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
package seeit3d.internal;

import org.eclipse.core.commands.Command;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.progress.UIJob;

import seeit3d.internal.base.SeeIT3DModule;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.ui.ide.commands.ChangeSortingPolyCylindersCriteriaCommand;
import seeit3d.internal.base.ui.ide.observers.WorkspaceClosedObserver;
import seeit3d.internal.base.visual.api.IColorScaleRegistry;
import seeit3d.internal.base.visual.api.IRelationshipsRegistry;
import seeit3d.internal.base.visual.colorscale.imp.BlueGreenRed;
import seeit3d.internal.base.visual.colorscale.imp.BlueToYellow;
import seeit3d.internal.base.visual.colorscale.imp.BlueTone;
import seeit3d.internal.base.visual.colorscale.imp.ColdToHotColorScale;
import seeit3d.internal.base.visual.colorscale.imp.GrayColorScale;
import seeit3d.internal.base.visual.colorscale.imp.HeatedObject;
import seeit3d.internal.base.visual.colorscale.imp.LinearOptimal;
import seeit3d.internal.base.visual.colorscale.imp.MagentaTone;
import seeit3d.internal.base.visual.colorscale.imp.Rainbow;
import seeit3d.internal.base.visual.relationships.imp.ArcBasedGenerator;
import seeit3d.internal.base.visual.relationships.imp.CommonBaseGenerator;
import seeit3d.internal.base.visual.relationships.imp.LineBaseGenerator;
import seeit3d.internal.base.visual.relationships.imp.MovementBasedGenerator;
import seeit3d.internal.base.visual.relationships.imp.NoRelationships;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Class in charge of loading contributions and initialize data for the plugin
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3D {

	private static Injector injector;

	public static void initialize() {
		try {
			injector = Guice.createInjector(new SeeIT3DModule());
			initializeColorScales();
			initializeRelationshipsGenerator();
			initializePreferences();
			initializeSortRadio();
			registerGlobalListener();
			ErrorHandler.setShell(new Shell(Display.getDefault()));
		} catch (Exception e) {
			ErrorHandler.error(e);
		}
	}

	private static void initializeColorScales() {
		IColorScaleRegistry registry = injector.getInstance(IColorScaleRegistry.class);
		registry.registerColorScale(new BlueTone());
		registry.registerColorScale(new BlueToYellow());
		registry.registerColorScale(new BlueGreenRed());
		registry.registerColorScale(new ColdToHotColorScale());
		registry.registerColorScale(new GrayColorScale());
		registry.registerColorScale(new HeatedObject());
		registry.registerColorScale(new LinearOptimal());
		registry.registerColorScale(new MagentaTone());
		registry.registerColorScale(new Rainbow());
	}

	private static void initializeRelationshipsGenerator() {
		IRelationshipsRegistry registry = injector.getInstance(IRelationshipsRegistry.class);
		registry.registerRelationshipGenerator(NoRelationships.class);
		registry.registerRelationshipGenerator(CommonBaseGenerator.class);
		registry.registerRelationshipGenerator(LineBaseGenerator.class);
		registry.registerRelationshipGenerator(ArcBasedGenerator.class);
		registry.registerRelationshipGenerator(MovementBasedGenerator.class);
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
