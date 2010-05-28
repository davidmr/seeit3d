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
package seeit3d.core.model.generator.metrics;

import java.util.ArrayList;
import java.util.List;

import seeit3d.general.error.exception.SeeIT3DMetricNotFoundException;

/**
 * Is where all the metrics instances live. Every metric within the plugin must be registered with this class in order to be accesible
 * 
 * @author David Montaño
 * 
 */
public class MetricsRegistry {

	private static final MetricsRegistry instance;

	static {
		instance = new MetricsRegistry();
	}

	public static MetricsRegistry getInstance() {
		return instance;
	}

	private final List<MetricCalculator> registeredMetrics = new ArrayList<MetricCalculator>();

	public void registerMetric(MetricCalculator metricToRegister) {
		for (MetricCalculator metric : registeredMetrics) {
			boolean sameName = metric.getMetricName().equals(metricToRegister.getMetricName());
			if (sameName && !metric.isReplaceable()) {
				throw new IllegalArgumentException("There is a metric with name: " + metricToRegister.getMetricName() + " already registered");
			}
		}
		registeredMetrics.add(metricToRegister);
	}

	public MetricCalculator getMetric(String metricName) {
		for (MetricCalculator metric : registeredMetrics) {
			if (metric.getMetricName().equals(metricName)) {
				return metric;
			}
		}
		throw new SeeIT3DMetricNotFoundException(metricName);
	}

	public void checkMetricRegistered(String metricName) {
		boolean exists = false;
		for (MetricCalculator metric : registeredMetrics) {
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
