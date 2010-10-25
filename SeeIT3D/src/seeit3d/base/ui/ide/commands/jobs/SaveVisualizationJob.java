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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.base.bus.events.SaveVisualizationEvent;
import seeit3d.base.error.ErrorHandler;
import seeit3d.utils.ViewConstants;

/**
 * Job to save a visualization in the background
 * 
 * @author David Montaño
 * 
 */
public class SaveVisualizationJob extends Job {

	private String filename;

	public SaveVisualizationJob(String filename) {
		super("Save visualization");
		this.filename = filename;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try {
			if (!filename.endsWith("." + ViewConstants.VISUALIZATION_EXTENSION)) {
				filename += "." + ViewConstants.VISUALIZATION_EXTENSION;
			}
			FileOutputStream output = new FileOutputStream(filename);
			publishEvent(new SaveVisualizationEvent(output));
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}

}