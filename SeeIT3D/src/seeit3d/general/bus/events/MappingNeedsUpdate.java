package seeit3d.general.bus.events;

import java.util.List;

import seeit3d.core.model.Container;
import seeit3d.general.bus.IEvent;

public class MappingNeedsUpdate implements IEvent {

	private final List<Container> containers;

	public MappingNeedsUpdate(List<Container> containers) {
		this.containers = containers;
	}

	public List<Container> getContainers() {
		return containers;
	}

}
