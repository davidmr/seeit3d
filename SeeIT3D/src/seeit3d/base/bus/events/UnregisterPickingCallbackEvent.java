package seeit3d.base.bus.events;

import seeit3d.base.bus.IEvent;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public class UnregisterPickingCallbackEvent implements IEvent {

	private final PickingCallback callback;

	public UnregisterPickingCallbackEvent(PickingCallback pickingCallbackToDelete) {
		this.callback = pickingCallbackToDelete;
	}

	public PickingCallback getCallback() {
		return callback;
	}

}
