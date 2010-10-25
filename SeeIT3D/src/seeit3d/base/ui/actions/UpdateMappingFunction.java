package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;
import seeit3d.base.model.VisualProperty;
import seeit3d.base.model.generator.metrics.MetricCalculator;

public class UpdateMappingFunction extends FunctionToApplyOnContainer {

	private final VisualProperty visualProp;
	private final MetricCalculator metric;

	public UpdateMappingFunction(VisualProperty visualProp, MetricCalculator metric) {
		this.visualProp = visualProp;
		this.metric = metric;
	}

	@Override
	public Container apply(Container container) {
		container.updateMapping(metric, visualProp);
		return container;
	}

}
