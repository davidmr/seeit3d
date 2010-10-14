package seeit3d.general.bus.events;

import java.awt.Rectangle;

import com.sun.j3d.utils.picking.PickCanvas;

import seeit3d.general.bus.IEvent;

public class SelectionToolEndedEvent implements IEvent {

	private final Rectangle selection;
	
	private final PickCanvas pickCanvas;

	public SelectionToolEndedEvent(Rectangle selection, PickCanvas pickCanvas) {
		this.selection = selection;
		this.pickCanvas = pickCanvas;
	}
	
	public PickCanvas getPickCanvas() {
		return pickCanvas;
	}

	public Rectangle getSelection() {
		return selection;
	}
}
