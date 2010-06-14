/**
 * Copyright (C) 2010  David Monta�o
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

import java.util.*;

import org.eclipse.jdt.core.*;

import seeit3d.general.model.Container;
import seeit3d.general.model.generator.IModelGenerator;
import seeit3d.modelers.java.generator.metrics.ComplexityCalculator;
import seeit3d.modelers.java.generator.metrics.LOCCalculator;

import com.google.common.collect.Lists;

/**
 * Package analyzer @see AbstracModelGenerator
 * 
 * @author David Monta�o
 * 
 */
public class PackageModelGenerator extends AbstracModelGenerator<IPackageFragment, ICompilationUnit> {

	public PackageModelGenerator(IPackageFragment pack) {
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