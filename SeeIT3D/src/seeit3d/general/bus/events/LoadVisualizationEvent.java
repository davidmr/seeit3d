package seeit3d.general.bus.events;

import java.io.InputStream;

import seeit3d.general.bus.IEvent;

public class LoadVisualizationEvent implements IEvent {

	private final InputStream input;

	public LoadVisualizationEvent(InputStream input) {
		this.input = input;
	}

	public InputStream getInput() {
		return input;
	}

}
