/**
 * 
 */
package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;
import seeit3d.base.model.generator.metrics.MetricCalculator;

public class RemoveMetricFromContainerFunction extends FunctionToApplyOnContainer {
	
	private final MetricCalculator metric;

	public RemoveMetricFromContainerFunction(MetricCalculator metric) {
		this.metric = metric;
	}

	@Override
	public Container apply(Container container) {
		container.removeFromMapping(metric);
		return container;
	}
}