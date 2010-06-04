package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class ChangeGranularityLevelEvent implements IEvent {

	private final boolean finer;

	public ChangeGranularityLevelEvent(boolean finer) {
		this.finer = finer;
	}

	public boolean isFiner() {
		return finer;
	}

}
