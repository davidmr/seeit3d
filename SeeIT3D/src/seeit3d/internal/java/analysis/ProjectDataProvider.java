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

import org.eclipse.jdt.core.IJavaElement;
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
import seeit3d.internal.java.analysis.metrics.LOCCalculator;
import seeit3d.internal.java.analysis.metrics.McCabeComplexityCalculator;
import seeit3d.internal.java.util.NameGenerator;

/**
 * Model data provider for projects
 * 
 * @author administrador
 * 
 */
public class ProjectDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof IJavaProject;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		IJavaProject project = (IJavaProject) element;
		return new JavaRepresentation(project);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		metrics.add(LOCCalculator.INSTANCE);
		metrics.add(McCabeComplexityCalculator.INSTANCE);
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		try {
			IJavaProject project = (IJavaProject) element;
			List<Child> packages = new ArrayList<Child>();
			IPackageFragment[] packageFragments = project.getPackageFragments();
			for (int i = 0; i < packageFragments.length; i++) {
				IPackageFragment fragment = packageFragments[i];
				IPackageFragmentRoot root = (IPackageFragmentRoot) fragment.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
				if (!fragment.isReadOnly() && !root.isArchive() && !root.isExternal()) {
					String name = NameGenerator.generateNameFor(fragment);
					Child child = new Child(name, fragment, new EclipseJavaRepresentation(fragment));
					packages.add(child);
				}
			}
			return packages;
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return Collections.emptyList();
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return JavaConstants.MODEL_PROVIDER_KEY_PACKAGE;
	}

	@Override
	public List<Object> related(Object element) {
		return Collections.emptyList();
	}

}
