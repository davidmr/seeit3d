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

import static com.google.common.collect.Lists.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.generator.annotation.PackageModeler;
import seeit3d.java.modeler.generator.metrics.annotation.LOC;
import seeit3d.java.modeler.generator.metrics.annotation.McCabeComplexity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Project analyzer @see AbstracModelGenerator
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class ProjectModelGenerator extends AbstracModelGenerator<IJavaProject, IPackageFragment> {

	private final List<MetricCalculator> metrics;

	@Inject
	public ProjectModelGenerator(
			@PackageModeler IModelGenerator<IPackageFragment> lowerLevelModelGenerator,
			@LOC MetricCalculator loc,
			@McCabeComplexity MetricCalculator mccabe) {
		super(lowerLevelModelGenerator);
		this.metrics = newArrayList(loc, mccabe);
	}

	@Override
	protected List<MetricCalculator> getMetrics() {
		return metrics;

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

}
