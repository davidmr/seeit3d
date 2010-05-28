package seeit3d.ui.ide.commands.jobs;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.general.SeeIT3DAPILocator;
import seeit3d.general.error.ErrorHandler;

public class LoadSavedVisualizationJob extends Job {

	private final SeeIT3DCore core;

	private final String filename;

	public LoadSavedVisualizationJob(String filename) {
		super("Load saved visualization");
		this.filename = filename;
		core = SeeIT3DAPILocator.findCore();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Loading", IProgressMonitor.UNKNOWN);
		try {
			FileInputStream input = new FileInputStream(filename);
			core.loadVisualization(input);
		} catch (FileNotFoundException e) {
			ErrorHandler.error("Visualization file not found");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while reading visualization file. Possibly wrong format or type");
			e.printStackTrace();
		}
		monitor.done();
		return Status.OK_STATUS;
	}

}
