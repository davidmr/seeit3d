package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.Container;

public class AddContainerEvent implements IEvent {

	private final Container container;

	public AddContainerEvent(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}
}
