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
package seeit3d.xml.modeler;

import java.util.*;

import seeit3d.base.model.*;
import seeit3d.base.model.Container;
import seeit3d.base.model.generator.IModelGenerator;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.xml.modeler.generator.metrics.XMLNominalMetricCalculator;
import seeit3d.xml.modeler.generator.metrics.XMLNumericMetricCalculator;
import seeit3d.xml.modeler.internal.*;

import com.google.inject.Singleton;

/**
 * Model generator for XML based visualization. It takes the model that was read by JAXB and translate it into the model of SeeIT3D
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class XMLBasedModelGenerator implements IModelGenerator<seeit3d.xml.modeler.internal.Container> {

	@Override
	public Container analize(seeit3d.xml.modeler.internal.Container containerXML, boolean includeDependecies) {
		IContainerRepresentedObject representedObject = new XMLBasedBasicRepresentation(containerXML);

		MetricsList metricsListXML = containerXML.getMetricsList();
		List<MetricCalculator> metrics = buildMetricsList(metricsListXML);

		Container analized = new Container(representedObject, metrics);
		analized.autoReferenceAsParentAndChild();

		List<Object> relatedXMLObjects = containerXML.getRelated();
		for (Object object : relatedXMLObjects) {
			seeit3d.xml.modeler.internal.Container relatedXML = (seeit3d.xml.modeler.internal.Container) object;
			Container relatedContainer = this.analize(relatedXML, false);
			analized.addRelatedContainer(relatedContainer);
		}

		List<Polycylinder> polycylindersXML = containerXML.getPolycylinder();
		for (Polycylinder polyXML : polycylindersXML) {
			MetricsValue metricsValue = polyXML.getMetricsValue();
			Map<MetricCalculator, String> metricsValues = buildMetricValues(metrics, metricsValue);
			PolyCylinder poly = new PolyCylinder("XML Polycylinder", metricsValues);
			analized.addPolyCylinder(poly);
		}

		return analized;
	}

	private List<MetricCalculator> buildMetricsList(MetricsList metricsListXML) {
		List<MetricDescription> metricsDescriptionXML = metricsListXML.getMetricDescription();
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
				metrics.add(new XMLNominalMetricCalculator(metricName, categories, categories.size()));
			}
		}
		return metrics;
	}

	private Map<MetricCalculator, String> buildMetricValues(List<MetricCalculator> metrics, MetricsValue metricsValue) {
		Map<MetricCalculator, String> result = new HashMap<MetricCalculator, String>();
		List<EntryMetricValue> entries = metricsValue.getEntryMetricValue();
		for (EntryMetricValue entry : entries) {
			String metricName = entry.getMetricName();
			MetricCalculator metric = findMetricByName(metrics, metricName);
			if (metric == null) {
				throw new IllegalArgumentException("Metric " + metricName + " is not defined in Metrics List");
			} else {
				result.put(metric, entry.getValue());
			}
		}
		return result;
	}

	private MetricCalculator findMetricByName(List<MetricCalculator> metrics, String name) {
		for (MetricCalculator metric : metrics) {
			if (metric.getMetricName().trim().equals(name)) {
				return metric;
			}
		}
		return null;
	}

}
