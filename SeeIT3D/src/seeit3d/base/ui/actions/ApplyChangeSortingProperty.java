package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;
import seeit3d.base.model.VisualProperty;

public class ApplyChangeSortingProperty extends FunctionToApplyOnContainer {

	private final VisualProperty property;

	public ApplyChangeSortingProperty(VisualProperty property) {
		this.property = property;
	}

	@Override
	public Container apply(Container container) {
		container.setSortingProperty(property);
		return container;
	}
}
