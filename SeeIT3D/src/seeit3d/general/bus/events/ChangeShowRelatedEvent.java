package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class ChangeShowRelatedEvent implements IEvent {

	private final boolean showRelated;

	public ChangeShowRelatedEvent(boolean showRelated) {
		this.showRelated = showRelated;
	}

	public boolean isShowRelated() {
		return showRelated;
	}

}
