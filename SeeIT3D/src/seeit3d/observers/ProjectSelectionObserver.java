package seeit3d.observers;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import seeit3d.manager.SeeIT3DManager;

public class ProjectSelectionObserver implements ISelectionListener {

	private final SeeIT3DManager manager;

	public ProjectSelectionObserver() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			Iterator<?> iterator = sel.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj instanceof IProject) {
					IProject project = (IProject) obj;
					manager.setCurrentProject(project.getName());
				}
				if (obj instanceof IJavaProject) {
					IJavaProject project = (IJavaProject) obj;
					manager.setCurrentProject(project.getElementName());
				}
			}
		}
	}

}
