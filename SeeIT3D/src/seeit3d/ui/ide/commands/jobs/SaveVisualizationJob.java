package seeit3d.ui.ide.commands.jobs;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.handler.error.ErrorHandler;
import seeit3d.utils.ViewConstants;

public class SaveVisualizationJob extends Job {

	private final SeeIT3DManager manager;

	private String filename;

	public SaveVisualizationJob(String filename) {
		super("Save visualization");
		this.filename = filename;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			if (!filename.endsWith("." + ViewConstants.VISUALIZATION_EXTENSION)) {
				filename += "." + ViewConstants.VISUALIZATION_EXTENSION;
			}
			FileOutputStream output = new FileOutputStream(filename);
			manager.saveVisualization(output);
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while writing visualization file");
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

}
