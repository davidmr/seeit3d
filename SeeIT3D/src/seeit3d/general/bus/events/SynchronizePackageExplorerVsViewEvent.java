package seeit3d.general.bus.events;

import java.util.List;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.PolyCylinder;

public class SynchronizePackageExplorerVsViewEvent implements IEvent {

	private final List<PolyCylinder> iteratorOnSelectedPolycylinders;

	public SynchronizePackageExplorerVsViewEvent(List<PolyCylinder> iteratorOnSelectedPolycylinders) {
		this.iteratorOnSelectedPolycylinders = iteratorOnSelectedPolycylinders;
	}

	public List<PolyCylinder> getIteratorOnSelectedPolycylinders() {
		return iteratorOnSelectedPolycylinders;
	}

}
