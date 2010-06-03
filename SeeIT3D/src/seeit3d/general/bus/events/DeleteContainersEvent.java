package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class DeleteContainersEvent implements IEvent {

	private final boolean all;

	public DeleteContainersEvent(boolean all) {
		this.all = all;
	}

	public boolean isAll() {
		return all;
	}

}
