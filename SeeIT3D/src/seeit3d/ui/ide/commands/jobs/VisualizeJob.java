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

import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.core.model.generator.IModelGenerator;
import seeit3d.general.SeeIT3DAPILocator;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.error.exception.SeeIT3DException;
import seeit3d.ui.ide.utils.OpenSeeIT3DView;

/**
 * Generic visualization job to show information in the visualization area. Takes <code>IModelGenerator</code> to analyze and register containers in the view.
 * 
 * @author David Montaño
 * 
 */
public class VisualizeJob extends Job {

	private final List<IModelGenerator> modelGenerators;

	private final SeeIT3DCore core;

	public VisualizeJob(Shell shell, List<IModelGenerator> modelGenerators) {
		super("Visualize in SeeIT 3D");
		this.modelGenerators = modelGenerators;
		core = SeeIT3DAPILocator.findCore();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		try {
			for (IModelGenerator model : modelGenerators) {
				model.analizeAndRegisterInView(true);
			}
		} catch (SeeIT3DException e) {
			ErrorHandler.error(e);
		}
		core.refreshVisualization();
		monitor.done();
		Display.getDefault().syncExec(new OpenSeeIT3DView());
		return Status.OK_STATUS;
	}

}
