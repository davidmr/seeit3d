package seeit3d.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.creator.MethodModelCreator;

public class VisualizeMethodInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		return new MethodModelCreator((IMethod) javaElement);
	}

}
