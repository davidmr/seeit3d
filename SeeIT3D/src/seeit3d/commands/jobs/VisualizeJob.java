package seeit3d.commands.jobs;

import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import seeit3d.error.ErrorHandler;
import seeit3d.error.exception.SeeIT3DException;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.model.IModelCreator;
import seeit3d.utils.OpenSeeIT3DView;

public class VisualizeJob extends Job {

	private final List<IModelCreator> modelCreators;

	private final SeeIT3DManager manager;

	public VisualizeJob(Shell shell, List<IModelCreator> modelCreators) {
		super("Visualize in SeeIT 3D");
		this.modelCreators = modelCreators;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Analizing", IProgressMonitor.UNKNOWN);
		try {
			for (IModelCreator model : modelCreators) {
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
