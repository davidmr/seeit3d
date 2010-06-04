package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class KeyBasedChangeSelectionEvent implements IEvent {

	private final boolean increase;

	public KeyBasedChangeSelectionEvent(boolean increase) {
		this.increase = increase;
	}

	public boolean isIncrease() {
		return increase;
	}

}
