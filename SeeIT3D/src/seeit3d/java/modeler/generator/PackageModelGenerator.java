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
package seeit3d.java.modeler.generator;

import java.util.*;

import org.eclipse.jdt.core.*;

import seeit3d.base.model.Container;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.generator.annotation.CompilationUnitModeler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Package analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class PackageModelGenerator extends AbstracModelGenerator<IPackageFragment, ICompilationUnit> {

	private final List<MetricCalculator> metrics;

	@Inject
	public PackageModelGenerator(@CompilationUnitModeler IModelGenerator<ICompilationUnit> lowerLeveIModelGenerator, @Named("metricsForTypes") List<MetricCalculator> metrics) {
		super(lowerLeveIModelGenerator);
		this.metrics = metrics;
	}

	@Override
	protected List<MetricCalculator> getMetrics() {
		return metrics;

	}

	@Override
	protected ICompilationUnit[] fetchChildren(IPackageFragment element) throws JavaModelException {
		return element.getCompilationUnits();
	}

	@Override
	protected void extraOperationsOnContainer(Container createdContainer, IPackageFragment element, boolean analizeDepedencies) throws JavaModelException {
		List<String> relatedPackages = new ArrayList<String>();
		if (analizeDepedencies) {
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
						Container relatedContainer = this.analize(packageFragment, false);
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
