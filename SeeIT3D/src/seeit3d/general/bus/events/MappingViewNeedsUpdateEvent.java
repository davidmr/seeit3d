package seeit3d.general.bus.events;

import java.util.List;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.Container;

public class MappingViewNeedsUpdateEvent implements IEvent {

	private final List<Container> containers;

	public MappingViewNeedsUpdateEvent(List<Container> containers) {
		this.containers = containers;
	}

	public List<Container> getContainers() {
		return containers;
	}

}
