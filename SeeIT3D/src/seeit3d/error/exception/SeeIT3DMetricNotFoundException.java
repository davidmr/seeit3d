package seeit3d.error.exception;

public class SeeIT3DMetricNotFoundException extends SeeIT3DException {

	private static final long serialVersionUID = 1L;

	public SeeIT3DMetricNotFoundException(String metricName) {
		super("Metric with name '" + metricName + "' not found in registry. Please register it before using the metric");

	}
}
