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
package seeit3d.model.java.creator;

import java.util.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.error.ErrorHandler;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.metrics.MetricsRegistry;
import seeit3d.model.ContainerRepresentedObject;
import seeit3d.model.IModelCreator;
import seeit3d.model.java.EclipseJavaResource;
import seeit3d.model.java.JavaRepresentation;
import seeit3d.model.representation.Container;
import seeit3d.model.representation.PolyCylinder;

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
public abstract class AbstracModelCreator<ElementToAnalize extends IJavaElement, Children extends IJavaElement> implements IModelCreator {

	private final SeeIT3DManager manager;

	private final ElementToAnalize elementToAnalize;

	protected boolean analizeDependecies = false;

	public AbstracModelCreator(ElementToAnalize elementToAnalize) {
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

	protected abstract IModelCreator lowerLevelModelCreator(Children childrenElement);

	protected void extraOperationsOnContainer(Container createdContainer, ElementToAnalize element) throws JavaModelException {
	};

	@Override
	public final Container analize(boolean analizeDependencies) {

		try {
			this.analizeDependecies = analizeDependencies;
			ContainerRepresentedObject representedObject = new JavaRepresentation(elementToAnalize);

			List<BaseMetricCalculator> metrics = buildMetrics();

			Children[] children = fetchChildren(elementToAnalize);

			Container container = new Container(representedObject, metrics);
			for (Children child : children) {

				IModelCreator lowerLevelModelCreator = lowerLevelModelCreator(child);

				Map<BaseMetricCalculator, String> metricsValues = new HashMap<BaseMetricCalculator, String>();
				for (BaseMetricCalculator metric : metrics) {
					metricsValues.put(metric, metric.calculateMetricValue(child));
				}

				PolyCylinder poly = new PolyCylinder(metricsValues, new EclipseJavaResource(child));
				container.addPolyCylinder(poly);

				if (lowerLevelModelCreator != null) {
					Container lowerLevelContainer = lowerLevelModelCreator.analize(false);
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

	private List<BaseMetricCalculator> buildMetrics() {
		List<String> metricNames = getMetricNames();
		MetricsRegistry registry = MetricsRegistry.getInstance();
		List<BaseMetricCalculator> metrics = new ArrayList<BaseMetricCalculator>();
		for (String name : metricNames) {
			metrics.add(registry.getMetric(name));
		}
		return metrics;
	}

}
