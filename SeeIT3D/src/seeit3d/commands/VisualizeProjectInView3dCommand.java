package seeit3d.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.creator.ProjectModelCreator;

public class VisualizeProjectInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		return new ProjectModelCreator((IJavaProject) javaElement);
	}

}
