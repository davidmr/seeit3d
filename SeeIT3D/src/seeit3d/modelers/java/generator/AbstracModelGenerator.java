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

import java.util.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.handler.error.ErrorHandler;
import seeit3d.core.model.*;
import seeit3d.core.model.generator.IModelGenerator;
import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.core.model.generator.metrics.MetricsRegistry;
import seeit3d.modelers.java.EclipseJavaResource;
import seeit3d.modelers.java.JavaRepresentation;

/**
 * This class is a template pattern to enable the creation of containers based on a <code>IJavaElement</code>.
 * 
 * @author David Montaño
 * 
 * @param <ElementToAnalize>
 *            Indicates the type of the element that is going to be analyzed by the implementing class
 * @param <Children>
 *            Indicates the type of children the class is going to handle. For example a package have class as children of it.
 */
public abstract class AbstracModelGenerator<ElementToAnalize extends IJavaElement, Children extends IJavaElement> implements IModelGenerator {

	private final SeeIT3DManager manager;

	private final ElementToAnalize elementToAnalize;

	protected boolean analizeDependecies = false;

	public AbstracModelGenerator(ElementToAnalize elementToAnalize) {
		this.elementToAnalize = elementToAnalize;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public final void analizeAndRegisterInView(boolean includeDependecies) {
		Container container = analize(includeDependecies);
		manager.addContainerToView(container);
	}

	protected abstract Children[] fetchChildren(ElementToAnalize element) throws JavaModelException;

	protected abstract List<String> getMetricNames();

	protected abstract IModelGenerator lowerLevelModelGenerator(Children childrenElement);

	protected void extraOperationsOnContainer(Container createdContainer, ElementToAnalize element) throws JavaModelException {
	};

	@Override
	public final Container analize(boolean analizeDependencies) {

		try {
			this.analizeDependecies = analizeDependencies;
			IContainerRepresentedObject representedObject = new JavaRepresentation(elementToAnalize);

			List<MetricCalculator> metrics = buildMetrics();

			Children[] children = fetchChildren(elementToAnalize);

			Container container = new Container(representedObject, metrics);
			for (Children child : children) {

				IModelGenerator lowerLevelModelGenerator = lowerLevelModelGenerator(child);

				Map<MetricCalculator, String> metricsValues = new HashMap<MetricCalculator, String>();
				for (MetricCalculator metric : metrics) {
					metricsValues.put(metric, metric.calculateMetricValue(child));
				}

				PolyCylinder poly = new PolyCylinder(metricsValues, new EclipseJavaResource(child));
				container.addPolyCylinder(poly);

				if (lowerLevelModelGenerator != null) {
					Container lowerLevelContainer = lowerLevelModelGenerator.analize(false);
					container.addChildrenContainer(lowerLevelContainer);
				}
			}

			extraOperationsOnContainer(container, elementToAnalize);
			this.analizeDependecies = false;

			return container;
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return null;
	}

	private List<MetricCalculator> buildMetrics() {
		List<String> metricNames = getMetricNames();
		MetricsRegistry registry = MetricsRegistry.getInstance();
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		for (String name : metricNames) {
			metrics.add(registry.getMetric(name));
		}
		return metrics;
	}

}
