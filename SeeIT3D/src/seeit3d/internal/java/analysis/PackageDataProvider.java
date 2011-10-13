/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.java.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.analysis.Child;
import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.java.JavaConstants;
import seeit3d.internal.java.analysis.metrics.JavaRepresentation;
import seeit3d.internal.java.analysis.metrics.LCOMCalculator;
import seeit3d.internal.java.analysis.metrics.LOCCalculator;
import seeit3d.internal.java.analysis.metrics.McCabeComplexityCalculator;
import seeit3d.internal.java.util.NameGenerator;

/**
 * Model data provider for packages
 * 
 * @author David Montaño
 * 
 */
public class PackageDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof IPackageFragment;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		IPackageFragment fragment = (IPackageFragment) element;
		return new JavaRepresentation(fragment);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		metrics.add(LOCCalculator.INSTANCE);
		metrics.add(McCabeComplexityCalculator.INSTANCE);
		metrics.add(LCOMCalculator.INSTANCE);
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		try {
			IPackageFragment fragment = (IPackageFragment) element;
			ICompilationUnit[] compilationUnits = fragment.getCompilationUnits();
			List<Child> children = new ArrayList<Child>();
			for (ICompilationUnit compilation : compilationUnits) {
				String name = NameGenerator.generateNameFor(compilation);
				Child child = new Child(name, compilation, new EclipseJavaRepresentation(compilation));
				children.add(child);
			}
			return children;
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return JavaConstants.MODEL_PROVIDER_KEY_TYPE;
	}

	@Override
	public List<Object> related(Object element) {
		try {
			List<Object> related = new ArrayList<Object>();
			List<String> analyzedPackageNames = new ArrayList<String>();
			IPackageFragment fragment = (IPackageFragment) element;
			for (ICompilationUnit javaFile : fragment.getCompilationUnits()) {
				IImportDeclaration[] imports = javaFile.getImports();
				for (IImportDeclaration imp : imports) {
					String packageName = imp.getElementName().substring(0, imp.getElementName().lastIndexOf("."));
					if (!analyzedPackageNames.contains(packageName)) {
						analyzedPackageNames.add(packageName);
						IPackageFragment relatedFragment = toPackageFragment(packageName, fragment.getJavaProject());
						if (relatedFragment != null) {
							related.add(relatedFragment);
						}
					}
				}
			}
			return related;
		} catch (Exception e) {
			ErrorHandler.error(e);
			return Collections.emptyList();
		}
	}

	private IPackageFragment toPackageFragment(String packageName, IJavaProject javaProject) throws JavaModelException {
		IPackageFragmentRoot[] allPackageFragmentRoots = javaProject.getAllPackageFragmentRoots();
		for (IPackageFragmentRoot fragmentRoot : allPackageFragmentRoots) {
			if (!fragmentRoot.isArchive()) {
				IPackageFragment packageFragment = fragmentRoot.getPackageFragment(packageName);
				if (packageFragment != null && packageFragment.exists()) {
					return packageFragment;
				}
			}
		}
		return null;
	}

}
