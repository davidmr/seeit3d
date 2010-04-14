package seeit3d.model.java.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.model.IModelCreator;
import seeit3d.model.java.metrics.ComplexityCalculator;
import seeit3d.model.java.metrics.LOCCalculator;
import seeit3d.model.representation.Container;

import com.google.common.collect.Lists;

public class PackageModelCreator extends AbstracModelCreator<IPackageFragment, ICompilationUnit> {

	public PackageModelCreator(IPackageFragment pack) {
		super(pack);
	}

	@Override
	protected List<String> getMetricNames() {
		return Lists.newArrayList(LOCCalculator.name, ComplexityCalculator.name);
	}

	@Override
	protected ICompilationUnit[] fetchChildren(IPackageFragment element) throws JavaModelException {
		return element.getCompilationUnits();
	}

	@Override
	protected IModelCreator lowerLevelModelCreator(ICompilationUnit childrenElement) {
		return new TypeClassModelCreator(childrenElement.findPrimaryType());
	}

	@Override
	protected void extraOperationsOnContainer(Container createdContainer, IPackageFragment element) throws JavaModelException {
		List<String> relatedPackages = new ArrayList<String>();
		if (analizeDependecies) {
			for (ICompilationUnit javaFile : element.getCompilationUnits()) {
				IImportDeclaration[] imports = javaFile.getImports();
				for (IImportDeclaration imp : imports) {
					String packageName = imp.getElementName().substring(0, imp.getElementName().lastIndexOf("."));
					if (!relatedPackages.contains(packageName)) {
						relatedPackages.add(packageName);
					}
				}
			}

		}
		analizeDependencies(createdContainer, relatedPackages, element.getJavaProject());
	}

	private void analizeDependencies(Container container, Collection<String> packagesNames, IJavaProject javaProject) throws JavaModelException {
		List<IPackageFragment> evaluatedPackages = new ArrayList<IPackageFragment>();
		for (String packageName : packagesNames) {
			IPackageFragmentRoot[] allPackageFragmentRoots = javaProject.getAllPackageFragmentRoots();
			for (IPackageFragmentRoot iPackageFragmentRoot : allPackageFragmentRoots) {
				if (!iPackageFragmentRoot.isArchive()) {
					IPackageFragment packageFragment = iPackageFragmentRoot.getPackageFragment(packageName);
					if (packageFragment != null && packageFragment.exists() && !evaluatedPackages.contains(packageFragment)) {
						PackageModelCreator modelCreator = new PackageModelCreator(packageFragment);
						Container relatedContainer = modelCreator.analize(false);
						if (relatedContainer != null) {
							container.addRelatedContainer(relatedContainer);
						}
						evaluatedPackages.add(packageFragment);
					}
				}
			}
		}
	}

}
