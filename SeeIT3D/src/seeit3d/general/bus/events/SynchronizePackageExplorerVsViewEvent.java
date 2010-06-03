package seeit3d.general.bus.events;

import java.util.Iterator;

import seeit3d.core.model.PolyCylinder;
import seeit3d.general.bus.IEvent;

public class SynchronizePackageExplorerVsViewEvent implements IEvent {

	private final Iterator<PolyCylinder> iteratorOnSelectedPolycylinders;

	public SynchronizePackageExplorerVsViewEvent(Iterator<PolyCylinder> iteratorOnSelectedPolycylinders) {
		this.iteratorOnSelectedPolycylinders = iteratorOnSelectedPolycylinders;
	}

	public Iterator<PolyCylinder> getIteratorOnSelectedPolycylinders() {
		return iteratorOnSelectedPolycylinders;
	}

}
