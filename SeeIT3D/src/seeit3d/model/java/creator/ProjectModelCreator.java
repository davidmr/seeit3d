package seeit3d.model.java.creator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.metrics.ComplexityCalculator;
import seeit3d.model.java.metrics.LOCCalculator;

import com.google.common.collect.Lists;

public class ProjectModelCreator extends AbstracModelCreator<IJavaProject, IPackageFragment> {

	public ProjectModelCreator(IJavaProject elementToAnalize) {
		super(elementToAnalize);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ComplexityCalculator.name);
	}

	@Override
	protected IPackageFragment[] fetchChildren(IJavaProject element) throws JavaModelException {
		List<IPackageFragment> packages = new ArrayList<IPackageFragment>();
		IPackageFragment[] packageFragments = element.getPackageFragments();
		for (int i = 0; i < packageFragments.length; i++) {
			IPackageFragment fragment = packageFragments[i];
			if (!fragment.isReadOnly()) {
				packages.add(fragment);
			}
		}
		return packages.toArray(new IPackageFragment[] {});
	}

	@Override
	protected IModelCreator lowerLevelModelCreator(IPackageFragment childrenElement) {
		return new PackageModelCreator(childrenElement);
	}

}
