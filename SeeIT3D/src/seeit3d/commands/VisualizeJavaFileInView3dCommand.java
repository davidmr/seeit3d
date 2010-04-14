package seeit3d.commands;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.creator.TypeClassModelCreator;

public class VisualizeJavaFileInView3dCommand extends AbstractVisualizeJavaElement {
	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		ICompilationUnit unit = (ICompilationUnit) javaElement;
		IModelCreator creator = new TypeClassModelCreator(unit.findPrimaryType());
		return creator;
	}

}
