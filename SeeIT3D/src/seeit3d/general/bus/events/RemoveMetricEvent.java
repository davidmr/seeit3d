package seeit3d.general.bus.events;

import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.general.bus.IEvent;

public class RemoveMetricEvent implements IEvent {

	private final MetricCalculator metric;

	public RemoveMetricEvent(MetricCalculator metric) {
		this.metric = metric;
	}

	public MetricCalculator getMetric() {
		return metric;
	}

}
