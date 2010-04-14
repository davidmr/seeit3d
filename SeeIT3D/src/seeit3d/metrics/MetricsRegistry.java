package seeit3d.metrics;

import java.util.ArrayList;
import java.util.List;

import seeit3d.error.exception.SeeIT3DMetricNotFoundException;

public class MetricsRegistry {

	private static final MetricsRegistry instance;

	static {
		instance = new MetricsRegistry();
	}

	public static MetricsRegistry getInstance() {
		return instance;
	}

	private final List<BaseMetricCalculator> registeredMetrics = new ArrayList<BaseMetricCalculator>();

	public void registerMetric(BaseMetricCalculator metricToRegister) {
		for (BaseMetricCalculator metric : registeredMetrics) {
			boolean sameName = metric.getMetricName().equals(metricToRegister.getMetricName());
			if (sameName && !metric.isReplaceable()) {
				throw new IllegalArgumentException("There is a metric with name: " + metricToRegister.getMetricName() + " already registered");
			}
		}
		registeredMetrics.add(metricToRegister);
	}

	public BaseMetricCalculator getMetric(String metricName) {
		for (BaseMetricCalculator metric : registeredMetrics) {
			if (metric.getMetricName().equals(metricName)) {
				return metric;
			}
		}
		throw new SeeIT3DMetricNotFoundException(metricName);
	}

	public void checkMetricRegistered(String metricName) {
		boolean exists = false;
		for (BaseMetricCalculator metric : registeredMetrics) {
			if (metric.getMetricName().equals(metricName)) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new SeeIT3DMetricNotFoundException(metricName);
		}

	}

}
