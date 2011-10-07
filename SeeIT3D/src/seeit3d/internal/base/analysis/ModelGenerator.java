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
package seeit3d.internal.base.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seeit3d.analysis.Child;
import seeit3d.analysis.IEclipseResourceRepresentation;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.IContainerRepresentedObject;
import seeit3d.internal.base.model.PolyCylinder;

/**
 * Class in charge of generating the visualization model using a specific IModelDataProvider
 * 
 * @author David Montaño
 * 
 */
public class ModelGenerator {

	private final IModelDataProvider provider;

	public ModelGenerator(IModelDataProvider provider) {
		this.provider = provider;
	}

	public final Container analize(Object element, boolean includeDependencies) {
		if (provider.accepts(element)) {

			IContainerRepresentedObject representedObject = provider.representedObject(element);
			List<MetricCalculator> metrics = provider.metrics(element);

			Container container = new Container(representedObject, metrics);

			List<Child> children = provider.children(element);

			for (Child child : children) {
				String name = child.getName();
				Object objectToEvaluate = child.getObject();
				Map<MetricCalculator, String> metricValues = new HashMap<MetricCalculator, String>();
				for (MetricCalculator metricCalculator : metrics) {
					String value = metricCalculator.calculate(objectToEvaluate);
					metricValues.put(metricCalculator, value);
				}
				IEclipseResourceRepresentation representation = child.getRepresentation();
				PolyCylinder poly = new PolyCylinder(name, metricValues, representation);
				container.addPolyCylinder(poly);
			}

			String childrenModelGeneratorKey = provider.getChildrenModelGeneratorKey();
			if (childrenModelGeneratorKey != null) {
				IModelDataProvider childrenProvider = SeeIT3D.getModelGenerator(childrenModelGeneratorKey);
				ModelGenerator childrenModelGenerator = new ModelGenerator(childrenProvider);
				for (Child child : children) {
					Container childContainer = childrenModelGenerator.analize(child.getObject(), false);
					container.addChildrenContainer(childContainer);
				}
			}

			if (includeDependencies) {
				List<Object> related = provider.related(element);
				for (Object relObject : related) {
					ModelGenerator relatedModelGenerator = new ModelGenerator(provider);
					Container relatedContainer = relatedModelGenerator.analize(relObject, false);
					if (relatedContainer != null) {
						container.addRelatedContainer(relatedContainer);
					}
				}
			}

			return container;
		}

		System.out.println("Provider " + provider.getClass() + " does not accept " + element);
		return null;
	}

}
