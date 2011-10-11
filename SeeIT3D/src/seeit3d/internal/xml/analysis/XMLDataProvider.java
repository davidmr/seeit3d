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
package seeit3d.internal.xml.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seeit3d.analysis.Child;
import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.NoEclipseResourceRepresentation;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.xml.analysis.metrics.XMLNominalMetricCalculator;
import seeit3d.internal.xml.analysis.metrics.XMLNumericMetricCalculator;
import seeit3d.internal.xml.internal.Container;
import seeit3d.internal.xml.internal.MetricDescription;
import seeit3d.internal.xml.internal.MetricsList;
import seeit3d.internal.xml.internal.Polycylinder;

/**
 * Model data provider for XML files, all the information is extracted from the file
 * 
 * @author David Montaño
 * 
 */
public class XMLDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof Container;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		Container containerXML = (Container) element;
		return new XMLBasedBasicRepresentation(containerXML);
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		Container containerXML = (Container) element;
		MetricsList metricsList = containerXML.getMetricsList();
		List<MetricDescription> metricsDescriptionXML = metricsList.getMetricDescription();
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		for (MetricDescription metricXML : metricsDescriptionXML) {
			String type = metricXML.getType();
			String metricName = metricXML.getValue();
			if (type.equals(MetricCalculator.NUMERIC)) {
				String max = metricXML.getMax();
				float maxValue = Float.parseFloat(max);
				metrics.add(new XMLNumericMetricCalculator(metricName, maxValue));
			} else if (type.equals(MetricCalculator.NOMINAL)) {
				if (metricXML.getCategories() == null) {
					throw new IllegalArgumentException("Categories must be defined in XML when using Categorized metrics");
				}
				List<String> categories = Arrays.asList(metricXML.getCategories().split(" "));
				metrics.add(new XMLNominalMetricCalculator(metricName, categories));
			}
		}
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		Container containerXML = (Container) element;
		List<Polycylinder> polycylinders = containerXML.getPolycylinder();
		List<Child> children = new ArrayList<Child>();
		for (Polycylinder polycylinder : polycylinders) {

			children.add(new Child(polycylinder.getName(), polycylinder, NoEclipseResourceRepresentation.INSTANCE));
		}
		return children;
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return null;
	}

	@Override
	public List<Object> related(Object element) {
		Container containerXML = (Container) element;
		return containerXML.getRelated();
	}

}
