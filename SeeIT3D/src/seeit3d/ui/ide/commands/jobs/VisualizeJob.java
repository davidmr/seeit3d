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
package seeit3d.ui.ide.commands.jobs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.events.AddContainerEvent;
import seeit3d.general.bus.events.OpenSeeIT3DViewEvent;
import seeit3d.general.bus.events.RefreshVisualizationEvent;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.error.exception.SeeIT3DException;
import seeit3d.general.model.Container;
import seeit3d.general.model.generator.IModelGenerator;

/**
 * Generic visualization job to show information in the visualization area. Takes <code>IModelGenerator</code> to analyze and register containers in the view.
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJob extends Job {

	private final List<IModelGenerator> modelGenerators;

	public VisualizeJob(Shell shell, List<IModelGenerator> modelGenerators) {
		super("Visualize in SeeIT 3D");
		this.modelGenerators = modelGenerators;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		List<Container> containers = new ArrayList<Container>();
		try {
			for (IModelGenerator model : modelGenerators) {
				Container container = model.analize(true);
				containers.add(container);
			}
		} catch (SeeIT3DException e) {
			ErrorHandler.error(e);
		}
		publishEvent(new AddContainerEvent(containers));
		publishEvent(new RefreshVisualizationEvent());
		publishEvent(new OpenSeeIT3DViewEvent());
		monitor.done();
		return Status.OK_STATUS;
	}

}
