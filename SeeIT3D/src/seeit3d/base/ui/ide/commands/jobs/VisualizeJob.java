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
package seeit3d.base.ui.ide.commands.jobs;

import static seeit3d.base.bus.EventBus.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Shell;

import seeit3d.base.bus.events.AddContainerEvent;
import seeit3d.base.bus.events.OpenSeeIT3DViewEvent;
import seeit3d.base.error.ErrorHandler;
import seeit3d.base.error.exception.SeeIT3DException;
import seeit3d.base.model.Container;
import seeit3d.base.model.generator.IModelGenerator;

/**
 * Generic visualization job to show information in the visualization area. Takes <code>IModelGenerator</code> to analyze and register containers in the view.
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJob<T> extends Job {

	private final IModelGenerator<T> generator;

	private final List<T> objects;

	public VisualizeJob(Shell shell, IModelGenerator<T> generator, List<T> objects) {
		super("Visualize in SeeIT 3D");
		this.generator = generator;
		this.objects = objects;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		List<Container> containers = new ArrayList<Container>();

		try {
			for (T object : objects) {
				Container container = generator.analize(object, true);
				containers.add(container);
			}
		} catch (SeeIT3DException e) {
			ErrorHandler.error(e);
		}
		publishEvent(new AddContainerEvent(containers));
		publishEvent(new OpenSeeIT3DViewEvent());
		monitor.done();
		return Status.OK_STATUS;
	}

}
