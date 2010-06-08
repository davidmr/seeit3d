package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.generator.metrics.MetricCalculator;

public class RemoveMetricEvent implements IEvent {

	private final MetricCalculator metric;

	public RemoveMetricEvent(MetricCalculator metric) {
		this.metric = metric;
	}

	public MetricCalculator getMetric() {
		return metric;
	}

}
