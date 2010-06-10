package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public class RegisterPickingCallbackEvent implements IEvent {

	private final PickingCallback callback;

	public RegisterPickingCallbackEvent(PickingCallback callback) {
		this.callback = callback;
	}

	public PickingCallback getCallback() {
		return callback;
	}

}
