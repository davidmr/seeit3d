package seeit3d.general.model.generator.metrics;

import java.util.*;

/**
 * This class standardizes the values from metrics (i.e. translate them to 0..1). It also maintains a cache of the metrics that were standardize before, in order to keep consistency between different
 * calculations of metric
 * 
 * @author David Montaño
 * 
 */
public class MetricsStandardizer {

	private static final Map<String, Map<String, Float>> classMetricsValuesMap;

	static {
		classMetricsValuesMap = Collections.synchronizedMap(new HashMap<String, Map<String, Float>>());
	}

	public static float getStandardizedValueFromMetric(MetricCalculator calculator, String value) {

		if (calculator == null) {
			return 0.0f;
		}
		if (calculator instanceof AbstractContinuousMetricCalculator) {
			AbstractContinuousMetricCalculator continuousCalculator = (AbstractContinuousMetricCalculator) calculator;
			float val = Float.parseFloat(value);
			float maxValue = continuousCalculator.getMaxValue();
			if (val > maxValue) {
				return 1.0f;
			} else {
				return val / maxValue;
			}
		} else if (calculator instanceof AbstractCategorizedMetricCalculator) {
			AbstractCategorizedMetricCalculator categorizedCalculator = (AbstractCategorizedMetricCalculator) calculator;
			Map<String, Float> metricsValues = classMetricsValuesMap.get(calculator.getMetricName());
			if (metricsValues == null) {
				metricsValues = initializeMapForMetricClass(categorizedCalculator);
				classMetricsValuesMap.put(calculator.getMetricName(), metricsValues);
			}
			return metricsValues.get(value);
		} else {
			return 0.0f;
		}
	}

	private static Map<String, Float> initializeMapForMetricClass(AbstractCategorizedMetricCalculator calculator) {
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
