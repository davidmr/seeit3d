package seeit3d.general.bus.events;

import seeit3d.core.model.Container;
import seeit3d.general.bus.IEvent;

public class AddContainerEvent implements IEvent {

	private final Container container;

	public AddContainerEvent(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}
}
