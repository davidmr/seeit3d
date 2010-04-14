package seeit3d.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import seeit3d.commands.jobs.VisualizeJob;
import seeit3d.model.IModelCreator;
import seeit3d.model.NoOpCreator;
import seeit3d.model.java.creator.PackageModelCreator;
import seeit3d.model.java.creator.ProjectModelCreator;
import seeit3d.model.java.creator.TypeClassModelCreator;

import com.google.common.collect.Lists;

public class VisualizeJavaElementFromEditor extends AbstractHandler {

	private static final String ELEMENT_TO_VISUALIZE = "seeit3d.visualizeFromEditorParameter";

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final String parameter = event.getParameter(ELEMENT_TO_VISUALIZE);
		IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
		final ICompilationUnit javaElement = (ICompilationUnit) JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());
		final Shell shell = HandlerUtil.getActiveShell(event);
		IModelCreator modelCreator = createModel(parameter, javaElement);
		VisualizeJob visualizeJob = new VisualizeJob(shell, Lists.newArrayList(modelCreator));
		visualizeJob.schedule();
		return null;
	}

	private IModelCreator createModel(String parameter, ICompilationUnit element) {
		if (parameter.equals("JAVA_FILE")) {
			return new TypeClassModelCreator(element.findPrimaryType());
		} else if (parameter.equals("PACKAGE")) {
			IPackageFragment packageFragment = element.findPrimaryType().getPackageFragment();
			return new PackageModelCreator(packageFragment);
		} else if (parameter.equals("PROJECT")) {
			return new ProjectModelCreator(element.getJavaProject());
		} else {
			return new NoOpCreator();
		}
	}

}
