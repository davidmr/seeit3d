package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;

public class SetSortingFunction extends FunctionToApplyOnContainer {

	@Override
	public Container apply(Container container) {
		container.setSorted(true);
		container.updateVisualRepresentation();
		return container;
	}

}
