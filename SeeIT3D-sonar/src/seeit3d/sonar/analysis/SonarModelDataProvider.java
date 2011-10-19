package seeit3d.sonar.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import seeit3d.analysis.Child;
import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.IModelDataProvider;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.sonar.SonarConstants;
import seeit3d.sonar.analysis.metrics.SonarNumericMetric;

public class SonarModelDataProvider implements IModelDataProvider {

	@Override
	public boolean accepts(Object element) {
		return element instanceof SonarArtifact;
	}

	@Override
	public IContainerRepresentedObject representedObject(Object element) {
		SonarArtifact artifact = (SonarArtifact) element;
		return new SonarRepresentation(artifact.getArtifactId());
	}

	@Override
	public List<MetricCalculator> metrics(Object element) {
		SonarArtifact artifact = (SonarArtifact) element;
		List<MetricCalculator> metrics = new ArrayList<MetricCalculator>();
		List<Metric> sonarMetrics = artifact.getMetrics();
		for (Metric metric : sonarMetrics) {
			metrics.add(new SonarNumericMetric(metric, artifact));
		}
		return metrics;
	}

	@Override
	public List<Child> children(Object element) {
		SonarArtifact artifact = (SonarArtifact) element;
		Sonar sonar = artifact.connect();
		ResourceQuery query = ResourceQuery.create(artifact.getArtifactId());
		query.setDepth(1);
		List<Resource> resources = sonar.findAll(query);
		List<Child> children = new ArrayList<Child>();
		for (Resource resource : resources) {
			children.add(new Child(resource.getKey(), artifact.cloneForArtifact(resource.getKey())));
		}
		return children;
	}

	@Override
	public String getChildrenModelGeneratorKey() {
		return SonarConstants.MODEL_GENERATOR_KEY;
	}

	@Override
	public List<Object> related(Object element) {
		return Collections.emptyList();
	}


}
