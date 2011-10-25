package seeit3d.sonar.analysis.metrics;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import seeit3d.analysis.metric.AbstractNumericMetricCalculator;
import seeit3d.sonar.analysis.SonarArtifact;

public class SonarNumericMetric extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = -7968909625953120141L;

	private final SonarArtifact data;

	private final Metric metric;

	public SonarNumericMetric(Metric metric, SonarArtifact data) {
		super(metric.getName());
		this.metric = metric;
		this.data = data;
	}

	@Override
	public Float calculateNumericValue(Object element) {
		SonarArtifact resource = (SonarArtifact) element;
		Sonar sonar = data.connect();
		Resource resourceMetric = sonar.find(ResourceQuery.createForMetrics(resource.getArtifactId(), metric.getKey()));
		if (resourceMetric != null) {
			Measure measure = resourceMetric.getMeasure(metric.getKey());
			if (measure != null && measure.getValue() != null) {
				return measure.getValue().floatValue();
			}
		}
		return DEFAULT_VALUE;
	}

}
