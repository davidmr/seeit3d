package seeit3d.ui.ide.commands.jobs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.events.SaveVisualizationEvent;
import seeit3d.general.error.ErrorHandler;
import seeit3d.utils.ViewConstants;

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
