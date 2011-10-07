/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.analysis.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seeit3d.analysis.metric.AbstractNominalMetricCalculator;
import seeit3d.analysis.metric.AbstractNumericMetricCalculator;
import seeit3d.analysis.metric.MetricCalculator;

/**
 * This class normalizes the values from metrics (i.e. translate them to 0..1)
 * 
 * @author David Montaño
 * 
 */
public class MetricsNormalizer {

	public static float getStandardizedValueFromMetric(MetricCalculator calculator, String value) {

		if (calculator == null) {
			return 0.0f;
		}
		if (calculator instanceof AbstractNumericMetricCalculator) {
			AbstractNumericMetricCalculator numericCalculator = (AbstractNumericMetricCalculator) calculator;
			float val = Float.parseFloat(value);
			float maxValue = numericCalculator.getMaxValue();
			if (val > maxValue) {
				return 1.0f;
			} else {
				return val / maxValue;
			}
		} else if (calculator instanceof AbstractNominalMetricCalculator) {
			AbstractNominalMetricCalculator nominalCalculator = (AbstractNominalMetricCalculator) calculator;
			Map<String, Float> metricsValues = valuesForNominalMetricCalculator(nominalCalculator);
			return metricsValues.get(value);
		} else {
			return 0.0f;
		}
	}

	private static Map<String, Float> valuesForNominalMetricCalculator(AbstractNominalMetricCalculator calculator) {

		Map<String, Float> metricValues = new HashMap<String, Float>();

		List<String> sortedCategories = new ArrayList<String>(calculator.getCategories());
		Collections.sort(sortedCategories);

		float maxValue = sortedCategories.size();
		float step = 1.0f / maxValue;
		float value = 0.0f;

		for (String category : sortedCategories) {
			metricValues.put(category, value);
			value += step;
		}

		return metricValues;
	}

}
