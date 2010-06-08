package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.PolyCylinder;

public class OpenEditorEvent implements IEvent {

	private final PolyCylinder polyCylinder;

	public OpenEditorEvent(PolyCylinder polyCylinder) {
		this.polyCylinder = polyCylinder;
	}

	public PolyCylinder getPolyCylinder() {
		return polyCylinder;
	}

}
