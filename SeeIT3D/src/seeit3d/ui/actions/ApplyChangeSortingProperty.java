package seeit3d.ui.actions;

import seeit3d.general.bus.utils.FunctionToApplyOnContainer;
import seeit3d.general.model.Container;
import seeit3d.general.model.VisualProperty;

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
