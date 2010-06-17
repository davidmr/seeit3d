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

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.Container;
import seeit3d.general.model.PolyCylinder;

/**
 * Event triggered when the selection in the visualization area is changed
 * 
 * @author David Montaño
 * 
 */
public class ChangeSelectionEvent implements IEvent {

	private final Container container;

	private final PolyCylinder polycylinder;

	private final boolean toggleContainerSelection;

	private final boolean togglePolycylinderSelection;

	public ChangeSelectionEvent(Container container, PolyCylinder polycylinder, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
		this.container = container;
		this.polycylinder = polycylinder;
		this.toggleContainerSelection = toggleContainerSelection;
		this.togglePolycylinderSelection = togglePolycylinderSelection;
	}

	public Container getContainer() {
		return container;
	}

	public PolyCylinder getPolycylinder() {
		return polycylinder;
	}

	public boolean isToggleContainerSelection() {
		return toggleContainerSelection;
	}

	public boolean isTogglePolycylinderSelection() {
		return togglePolycylinderSelection;
	}

}
