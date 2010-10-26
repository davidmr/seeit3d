/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.base.bus.events;

import java.awt.Rectangle;

import seeit3d.base.bus.IEvent;

import com.sun.j3d.utils.picking.PickCanvas;

/**
 * This event indicates that the selection tool has ended its function i.e. the moused was dragged using the selection tool
 * 
 * @author David Montaño
 * 
 */
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
