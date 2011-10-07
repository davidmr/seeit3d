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
package seeit3d.internal.base.ui.jobs;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.internal.base.bus.events.LoadVisualizationEvent;
import seeit3d.internal.base.error.ErrorHandler;

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
