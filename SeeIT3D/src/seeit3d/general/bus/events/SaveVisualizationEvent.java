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
package seeit3d.general.bus.events;

import java.io.OutputStream;

import seeit3d.general.bus.IEvent;

/**
 * Event triggered when the visualization is going to be saved
 * 
 * @author David Montaño
 * 
 */
public class SaveVisualizationEvent implements IEvent {

	private final OutputStream output;

	public SaveVisualizationEvent(OutputStream output) {
		this.output = output;
	}

	public OutputStream getOutput() {
		return output;
	}

}
