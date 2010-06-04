package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class ScaleContainerEvent implements IEvent {

	private final boolean up;

	public ScaleContainerEvent(boolean up) {
		this.up = up;
	}

	public boolean isUp() {
		return up;
	}

}
