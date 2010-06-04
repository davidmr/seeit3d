package seeit3d.general.bus.events;

import seeit3d.core.model.Container;
import seeit3d.core.model.PolyCylinder;
import seeit3d.general.bus.IEvent;

public class ChangeSelectionEvent implements IEvent {

	private final Container container;

	private final PolyCylinder polycylinder;

	private final boolean toggleContainerSelection;

	private final boolean togglePolycylinderSelection;

	public ChangeSelectionEvent(Container container, PolyCylinder polycylinder, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
		this.container = container;
		this.polycylinder = polycylinder;
		this.toggleContainerSelection = toggleContainerSelection;
		this.togglePolycylinderSelection = togglePolycylinderSelection;
	}

	public Container getContainer() {
		return container;
	}

	public PolyCylinder getPolycylinder() {
		return polycylinder;
	}

	public boolean isToggleContainerSelection() {
		return toggleContainerSelection;
	}

	public boolean isTogglePolycylinderSelection() {
		return togglePolycylinderSelection;
	}

}
