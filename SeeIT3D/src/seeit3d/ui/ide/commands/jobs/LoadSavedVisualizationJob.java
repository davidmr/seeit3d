package seeit3d.ui.ide.commands.jobs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.general.bus.EventBus;
import seeit3d.general.bus.events.LoadVisualizationEvent;
import seeit3d.general.error.ErrorHandler;

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
			EventBus.publishEvent(new LoadVisualizationEvent(input));
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}

}
