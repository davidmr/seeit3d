package seeit3d.general.bus.events;

import seeit3d.core.model.VisualProperty;
import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.general.bus.IEvent;

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
