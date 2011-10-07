/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.bus.events;

import java.awt.Rectangle;

import seeit3d.internal.base.bus.IEvent;

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
