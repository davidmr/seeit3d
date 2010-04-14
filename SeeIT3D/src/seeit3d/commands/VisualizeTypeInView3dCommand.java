package seeit3d.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.creator.TypeClassModelCreator;

public class VisualizeTypeInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		IType type = (IType) javaElement;
		return new TypeClassModelCreator(type);
	}

}
