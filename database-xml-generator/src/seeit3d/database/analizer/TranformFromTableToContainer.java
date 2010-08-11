package seeit3d.database.analizer;

import java.util.List;

import seeit3d.database.xml.model.*;

import com.google.common.base.Function;

public class TranformFromTableToContainer implements Function<Table, Container> {

	@Override
	public Container apply(Table table) {
		Container container = new Container();
		container.setGranularityLevelName("column");

		Mapping mapping = buildDefaultMapping();
		container.setMapping(mapping);

		MetricsList metrics = buildMetricList();
		container.setMetricsList(metrics);

		container.setName(table.getName());
		container.setVisible(true);

		addPolycylinders(table, container);

		return container;
	}

	private void addPolycylinders(Table table, Container container) {
		List<Polycylinder> polycylinders = container.getPolycylinder();
		for (Column column : table.getColumns()) {
			Polycylinder polycylinder = new Polycylinder();
			MetricsValue metricValues = buildEntryMetricValue(table, column);
			polycylinder.setMetricsValue(metricValues);
			polycylinder.setName(column.getName());
			polycylinders.add(polycylinder);
		}
	}

	private MetricsValue buildEntryMetricValue(Table table, Column column) {
		MetricsValue metricValues = new MetricsValue();

		List<EntryMetricValue> entriesMetricValue = metricValues.getEntryMetricValue();

		EntryMetricValue metricValue1 = new EntryMetricValue();
		metricValue1.setMetricName("rowNum");
		metricValue1.setValue(String.valueOf(table.getRowNum()));

		EntryMetricValue metricValue2 = new EntryMetricValue();
		metricValue2.setMetricName("dataType");
		metricValue2.setValue(column.getType());

		entriesMetricValue.add(metricValue1);
		entriesMetricValue.add(metricValue2);
		return metricValues;
	}

	private MetricsList buildMetricList() {
		MetricsList metrics = new MetricsList();
		List<MetricDescription> metricsDescription = metrics.getMetricDescription();
		MetricDescription metric1 = new MetricDescription();
		List<String> sqlTypesAsString = Util.SQLTypesAsString();
		String categories = "";
		for (String type : sqlTypesAsString) {
			categories += type + " ";
		}
		categories = categories.substring(0, categories.length() - 1);
		metric1.setCategories(categories);
		metric1.setMax(String.valueOf(sqlTypesAsString.size()));
		metric1.setType("CATEGORIZED");
		metric1.setValue("dataType");

		MetricDescription metric2 = new MetricDescription();
		metric2.setMax("1000");
		metric2.setType("CONTINUOUS");
		metric2.setValue("rowNum");

		metricsDescription.add(metric1);
		metricsDescription.add(metric2);
		return metrics;
	}

	private Mapping buildDefaultMapping() {
		Mapping mapping = new Mapping();

		List<MappingValue> mappingValues = mapping.getMappingValue();

		MappingValue mappingValue1 = new MappingValue();
		mappingValue1.setMetricName("rowNum");
		mappingValue1.setVisualProperty("HEIGHT");

		MappingValue mappingValue2 = new MappingValue();
		mappingValue2.setMetricName("dataType");
		mappingValue2.setVisualProperty("COLOR");

		mappingValues.add(mappingValue1);
		mappingValues.add(mappingValue2);
		return mapping;
	}

}
