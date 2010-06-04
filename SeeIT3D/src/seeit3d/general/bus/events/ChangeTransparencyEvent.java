package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

public class ChangeTransparencyEvent implements IEvent {

	private final boolean moreTransparent;

	public ChangeTransparencyEvent(boolean moreTransparent) {
		this.moreTransparent = moreTransparent;
	}

	public boolean isMoreTransparent() {
		return moreTransparent;
	}

}
