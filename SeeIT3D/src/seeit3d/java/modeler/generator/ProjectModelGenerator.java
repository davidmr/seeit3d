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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.*;

import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.generator.annotation.PackageModeler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

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
	public ProjectModelGenerator(@PackageModeler IModelGenerator<IPackageFragment> lowerLevelModelGenerator, @Named(value = "metricsForPackagesAndMethods") List<MetricCalculator> metrics) {
		super(lowerLevelModelGenerator);
		this.metrics = metrics;
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
