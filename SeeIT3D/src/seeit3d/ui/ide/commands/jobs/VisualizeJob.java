/**
 * Copyright (C) 2010  David Monta�o
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

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.handler.error.ErrorHandler;
import seeit3d.core.handler.error.exception.SeeIT3DException;
import seeit3d.core.model.generator.IModelGenerator;
import seeit3d.ui.ide.utils.OpenSeeIT3DView;

/**
 * Generic visualization job to show information in the visualization area. Takes <code>IModelCreator</code> to analyze and register containers in the view.
 * 
 * @author David Monta�o
 * 
 */
public class VisualizeJob extends Job {

	private final List<IModelGenerator> modelCreators;

	private final SeeIT3DManager manager;

	public VisualizeJob(Shell shell, List<IModelGenerator> modelCreators) {
		super("Visualize in SeeIT 3D");
		this.modelCreators = modelCreators;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		try {
			for (IModelGenerator model : modelCreators) {
				model.analizeAndRegisterInView(true);
			}
		} catch (SeeIT3DException e) {
			ErrorHandler.error(e);
		}
		manager.refreshVisualization();
		monitor.done();
		Display.getDefault().syncExec(new OpenSeeIT3DView());
		return Status.OK_STATUS;
	}

}