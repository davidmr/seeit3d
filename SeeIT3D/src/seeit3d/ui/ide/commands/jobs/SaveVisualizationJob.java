package seeit3d.ui.ide.commands.jobs;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.general.SeeIT3DAPILocator;
import seeit3d.general.error.ErrorHandler;
import seeit3d.utils.ViewConstants;

public class SaveVisualizationJob extends Job {

	private final SeeIT3DCore core;

	private String filename;

	public SaveVisualizationJob(String filename) {
		super("Save visualization");
		this.filename = filename;
		core = SeeIT3DAPILocator.findCore();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
		try {
			if (!filename.endsWith("." + ViewConstants.VISUALIZATION_EXTENSION)) {
				filename += "." + ViewConstants.VISUALIZATION_EXTENSION;
			}
			FileOutputStream output = new FileOutputStream(filename);
			core.saveVisualization(output);
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while writing visualization file");
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}

}
