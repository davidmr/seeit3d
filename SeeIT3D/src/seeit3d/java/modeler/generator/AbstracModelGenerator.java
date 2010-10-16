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

import static seeit3d.base.bus.EventBus.*;

import java.util.*;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.base.bus.events.AddContainerEvent;
import seeit3d.base.error.ErrorHandler;
import seeit3d.base.model.*;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.java.modeler.EclipseJavaResource;
import seeit3d.java.modeler.JavaRepresentation;

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
public abstract class AbstracModelGenerator<ElementToAnalize extends IJavaElement, Children extends IJavaElement> implements IModelGenerator<ElementToAnalize> {

	private final IModelGenerator<Children> lowerLevelModelGenerator;

	public AbstracModelGenerator(IModelGenerator<Children> lowerLeveIModelGenerator) {
		this.lowerLevelModelGenerator = lowerLeveIModelGenerator;
	}

	protected abstract Children[] fetchChildren(ElementToAnalize element) throws JavaModelException;

	protected abstract List<MetricCalculator> getMetrics();

	protected void extraOperationsOnContainer(Container createdContainer, ElementToAnalize element, boolean analizeDependencies) throws JavaModelException {};

	public Container analize(ElementToAnalize elementToAnalize, boolean analizeDependencies) {
		try {
			IContainerRepresentedObject representedObject = new JavaRepresentation(elementToAnalize);

			List<MetricCalculator> metrics = getMetrics();

			Children[] children = fetchChildren(elementToAnalize);

			Container container = new Container(representedObject, metrics);
			for (Children child : children) {

				Map<MetricCalculator, String> metricsValues = new HashMap<MetricCalculator, String>();
				for (MetricCalculator metric : metrics) {
					metricsValues.put(metric, metric.calculateMetricValue(child));
				}

				String polycylinderName = "NO_NAME";
				if (lowerLevelModelGenerator != null) {
					Container lowerLevelContainer = lowerLevelModelGenerator.analize(child, false);
					container.addChildrenContainer(lowerLevelContainer);
					polycylinderName = lowerLevelContainer.getName();
				}

				PolyCylinder poly = new PolyCylinder(polycylinderName, metricsValues, new EclipseJavaResource(child));
				container.addPolyCylinder(poly);
			}

			extraOperationsOnContainer(container, elementToAnalize, analizeDependencies);

			return container;
		} catch (JavaModelException e) {
			ErrorHandler.error(e);
		}
		return null;
	}

	public void analizeAndRegisterInView(ElementToAnalize element, boolean includeDependecies) {
		Container container = analize(element, includeDependecies);
		publishEvent(new AddContainerEvent(Arrays.asList(container)));
	}

}
