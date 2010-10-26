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
package seeit3d.base.model.generator.metrics;

import java.util.*;

/**
 * This class normalizes the values from metrics (i.e. translate them to 0..1). It also maintains a cache of the metrics that were standardize before, in order to keep consistency between different
 * calculations of metric
 * 
 * @author David Montaño
 * 
 */
public class MetricsNormalizer {

	private static final Map<String, Map<String, Float>> classMetricsValuesMap;

	static {
		classMetricsValuesMap = Collections.synchronizedMap(new HashMap<String, Map<String, Float>>());
	}

	public static float getStandardizedValueFromMetric(MetricCalculator calculator, String value) {

		if (calculator == null) {
			return 0.0f;
		}
		if (calculator instanceof AbstractNumericMetricCalculator) {
			AbstractNumericMetricCalculator continuousCalculator = (AbstractNumericMetricCalculator) calculator;
			float val = Float.parseFloat(value);
			float maxValue = continuousCalculator.getMaxValue();
			if (val > maxValue) {
				return 1.0f;
			} else {
				return val / maxValue;
			}
		} else if (calculator instanceof AbstractNominalMetricCalculator) {
			AbstractNominalMetricCalculator nominalCalculator = (AbstractNominalMetricCalculator) calculator;
			Map<String, Float> metricsValues = classMetricsValuesMap.get(calculator.getMetricName());
			if (metricsValues == null) {
				metricsValues = initializeMapForMetricClass(nominalCalculator);
				classMetricsValuesMap.put(calculator.getMetricName(), metricsValues);
			}
			return metricsValues.get(value);
		} else {
			return 0.0f;
		}
	}

	private static Map<String, Float> initializeMapForMetricClass(AbstractNominalMetricCalculator calculator) {
		Map<String, Float> metricValues = new HashMap<String, Float>();
		float maxValue = calculator.getNumCategories();
		float step = 1.0f / maxValue;
		float value = 0.0f;
		for (String category : calculator.getCategories()) {
			metricValues.put(category, value);
			value += step;
		}
		return metricValues;
	}

}
