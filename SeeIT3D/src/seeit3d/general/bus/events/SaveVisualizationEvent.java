package seeit3d.general.bus.events;

import java.io.OutputStream;

import seeit3d.general.bus.IEvent;

public class SaveVisualizationEvent implements IEvent {

	private final OutputStream output;

	public SaveVisualizationEvent(OutputStream output) {
		this.output = output;
	}

	public OutputStream getOutput() {
		return output;
	}

}
