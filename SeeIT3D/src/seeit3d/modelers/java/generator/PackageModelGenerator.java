/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.modelers.java.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.*;

import seeit3d.general.model.Container;
import seeit3d.general.model.generator.IModelGenerator;
import seeit3d.general.model.generator.metrics.MetricCalculator;
import seeit3d.modelers.java.generator.metrics.ComplexityCalculator;
import seeit3d.modelers.java.generator.metrics.LOCCalculator;
import seeit3d.modelers.java.generator.metrics.LOCMCalculator;

/**
 * Package analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
public class PackageModelGenerator extends AbstracModelGenerator<IPackageFragment, ICompilationUnit> {

	public PackageModelGenerator(IPackageFragment pack) {
		super(pack);
	}

	@Override
	protected List<Class<? extends MetricCalculator>> getMetricNames() {
		List<Class<? extends MetricCalculator>> classes = new ArrayList<Class<? extends MetricCalculator>>();
		classes.add(LOCCalculator.class);
		classes.add(ComplexityCalculator.class);
		classes.add(LOCMCalculator.class);
		return classes;
	}

	@Override
	protected ICompilationUnit[] fetchChildren(IPackageFragment element) throws JavaModelException {
		return element.getCompilationUnits();
	}

	@Override
	protected IModelGenerator lowerLevelModelGenerator(ICompilationUnit childrenElement) {
		if (childrenElement.findPrimaryType() != null) {
			return new TypeClassModelGenerator(childrenElement.findPrimaryType());
		} else {
			return null;
		}
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
						PackageModelGenerator modelGenerator = new PackageModelGenerator(packageFragment);
						Container relatedContainer = modelGenerator.analize(false);
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
