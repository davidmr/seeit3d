package seeit3d.ui.ide.commands.jobs;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.handler.error.ErrorHandler;

public class LoadSavedVisualizationJob extends Job {

	private final SeeIT3DManager manager;

	private String filename;

	public LoadSavedVisualizationJob(String filename) {
		super("Load saved visualization");
		this.filename = filename;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			FileInputStream input = new FileInputStream(filename);
			manager.loadVisualization(input);
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while reading visualization file. Possibly wrong format or type");
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}

}
