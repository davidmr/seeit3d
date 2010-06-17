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

import static seeit3d.general.bus.EventBus.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.general.bus.events.LoadVisualizationEvent;
import seeit3d.general.error.ErrorHandler;

/**
 * Job to load a saved visualization in the background
 * 
 * @author David Montaño
 * 
 */
public class LoadSavedVisualizationJob extends Job {

	private final String filename;

	public LoadSavedVisualizationJob(String filename) {
		super("Load saved visualization");
		this.filename = filename;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Loading", IProgressMonitor.UNKNOWN);
		try {
			FileInputStream input = new FileInputStream(filename);
			publishEvent(new LoadVisualizationEvent(input));
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}

}
