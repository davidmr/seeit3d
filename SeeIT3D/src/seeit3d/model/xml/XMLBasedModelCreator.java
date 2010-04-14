package seeit3d.model.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.model.ContainerRepresentedObject;
import seeit3d.model.IModelCreator;
import seeit3d.model.representation.Container;
import seeit3d.model.representation.PolyCylinder;
import seeit3d.model.xml.internal.EntryMetricValue;
import seeit3d.model.xml.internal.MetricDescription;
import seeit3d.model.xml.internal.MetricsList;
import seeit3d.model.xml.internal.MetricsValue;
import seeit3d.model.xml.internal.Polycylinder;
import seeit3d.model.xml.metrics.XMLCategorizedMetricCalculator;
import seeit3d.model.xml.metrics.XMLContinuousMetricCalculator;

public class XMLBasedModelCreator implements IModelCreator {

	private final SeeIT3DManager manager;

	private final seeit3d.model.xml.internal.Container containerXML;

	public XMLBasedModelCreator(seeit3d.model.xml.internal.Container container) {
		this.containerXML = container;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Container analize(boolean includeDependecies) {
		ContainerRepresentedObject representedObject = new XMLBasedBasicRepresentation(containerXML);

		MetricsList metricsListXML = containerXML.getMetricsList();
		List<BaseMetricCalculator> metrics = buildMetricsList(metricsListXML);

		Container analized = new Container(representedObject, metrics);
		analized.autoReference();

		List<Polycylinder> polycylindersXML = containerXML.getPolycylinder();
		for (Polycylinder polyXML : polycylindersXML) {
			MetricsValue metricsValue = polyXML.getMetricsValue();
			Map<BaseMetricCalculator, String> metricsValues = buildMetricValues(metrics, metricsValue);
			PolyCylinder poly = new PolyCylinder(metricsValues);
			analized.addPolyCylinder(poly);
		}

		return analized;
	}

	private List<BaseMetricCalculator> buildMetricsList(MetricsList metricsListXML) {
		List<MetricDescription> metricsDescriptionXML = metricsListXML.getMetricDescription();
		List<BaseMetricCalculator> metrics = new ArrayList<BaseMetricCalculator>();
		for (MetricDescription metricXML : metricsDescriptionXML) {
			String type = metricXML.getType();
			String metricName = metricXML.getValue();

			if (type.equals(BaseMetricCalculator.CONTINUOUS)) {
				String max = metricXML.getMax();
				float maxValue = Float.parseFloat(max);
				metrics.add(new XMLContinuousMetricCalculator(metricName, maxValue));
			} else if (type.equals(BaseMetricCalculator.CATEGORIZED)) {
				if (metricXML.getCategories() == null) {
					throw new IllegalArgumentException("categories must be defined in XML when using Categorized metrics");
				}
				List<String> categories = Arrays.asList(metricXML.getCategories().split(" "));
				metrics.add(new XMLCategorizedMetricCalculator(metricName, categories, categories.size()));
			}
		}
		return metrics;
	}

	private Map<BaseMetricCalculator, String> buildMetricValues(List<BaseMetricCalculator> metrics, MetricsValue metricsValue) {
		Map<BaseMetricCalculator, String> result = new HashMap<BaseMetricCalculator, String>();
		List<EntryMetricValue> entries = metricsValue.getEntryMetricValue();
		for (EntryMetricValue entry : entries) {
			String metricName = entry.getMetricName();
			BaseMetricCalculator metric = findMetricByName(metrics, metricName);
			if (metric == null) {
				throw new IllegalArgumentException("Metric " + metricName + " is not defined in Metrics List");
			} else {
				result.put(metric, entry.getValue());
			}
		}
		return result;
	}

	private BaseMetricCalculator findMetricByName(List<BaseMetricCalculator> metrics, String name) {
		for (BaseMetricCalculator metric : metrics) {
			if (metric.getMetricName().equals(name)) {
				return metric;
			}
		}
		return null;
	}

	@Override
	public void analizeAndRegisterInView(boolean includeDependecies) {
		Container container = analize(includeDependecies);
		manager.addContainerToView(container);

	}

}
