package seeit3d.commands;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;

import seeit3d.model.IModelCreator;
import seeit3d.model.NoOpCreator;
import seeit3d.model.java.creator.PackageModelCreator;

public class VisualizePackageInView3dCommand extends AbstractVisualizeJavaElement {

	@Override
	protected IModelCreator createModel(IJavaElement javaElement) {
		IJavaElement packageToAnalyze = (IPackageFragment) javaElement;
		if (!packageToAnalyze.isReadOnly()) {
			return new PackageModelCreator((IPackageFragment) packageToAnalyze);
		} else {
			return new NoOpCreator();
		}

	}

}
