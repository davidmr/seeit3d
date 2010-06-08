package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.VisualProperty;
import seeit3d.general.model.generator.metrics.MetricCalculator;

public class MappingChangedEvent implements IEvent {

	private final VisualProperty visualProperty;

	private final MetricCalculator metric;

	public MappingChangedEvent(VisualProperty visualProperty, MetricCalculator metric) {
		this.visualProperty = visualProperty;
		this.metric = metric;
	}

	public VisualProperty getVisualProperty() {
		return visualProperty;
	}

	public MetricCalculator getMetricCalculator() {
		return metric;
	}

}
