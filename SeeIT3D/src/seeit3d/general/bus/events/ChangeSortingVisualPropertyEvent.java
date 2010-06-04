package seeit3d.general.bus.events;

import seeit3d.core.model.VisualProperty;
import seeit3d.general.bus.IEvent;

public class ChangeSortingVisualPropertyEvent implements IEvent {

	private final VisualProperty visualProperty;

	public ChangeSortingVisualPropertyEvent(VisualProperty visualProperty) {
		this.visualProperty = visualProperty;
	}

	public VisualProperty getVisualProperty() {
		return visualProperty;
	}

}
