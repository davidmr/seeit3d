package seeit3d.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.commands.jobs.VisualizeJob;
import seeit3d.model.IModelCreator;

public abstract class AbstractVisualizeJavaElement extends AbstractHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		List<IModelCreator> modelCreators = new ArrayList<IModelCreator>();
		ITreeSelection currentSelection = (ITreeSelection) HandlerUtil.getCurrentSelection(event);
		for (Iterator<Object> iterator = currentSelection.iterator(); iterator.hasNext();) {
			IJavaElement javaElement = (IJavaElement) iterator.next();
			IModelCreator modelCreator = createModel(javaElement);
			modelCreators.add(modelCreator);

		}
		Shell shell = HandlerUtil.getActiveShell(event);
		Job visualizeJob = new VisualizeJob(shell, modelCreators);
		visualizeJob.schedule();
		return null;
	}

	protected abstract IModelCreator createModel(IJavaElement javaElement);

}
