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

import java.util.List;

import seeit3d.base.bus.IEvent;
import seeit3d.base.model.Container;
import seeit3d.base.model.PolyCylinder;

/**
 * Event triggered when the selection in the visualization area is changed
 * 
 * @author David Montaño
 * 
 */
public class ChangeSelectionEvent implements IEvent {

	private final List<Container> containers;

	private final List<PolyCylinder> polycylinders;

	private final boolean toggleContainerSelection;

	private final boolean togglePolycylinderSelection;

	public ChangeSelectionEvent(List<Container> containers, List<PolyCylinder> polycylinders, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
		this.containers = containers;
		this.polycylinders = polycylinders;
		this.toggleContainerSelection = toggleContainerSelection;
		this.togglePolycylinderSelection = togglePolycylinderSelection;
	}

	public List<Container> getContainers() {
		return containers;
	}

	public List<PolyCylinder> getPolycylinders() {
		return polycylinders;
	}

	public boolean isToggleContainerSelection() {
		return toggleContainerSelection;
	}

	public boolean isTogglePolycylinderSelection() {
		return togglePolycylinderSelection;
	}

}
