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
package seeit3d.base.core.api;

import java.util.List;

import seeit3d.base.model.Container;
import seeit3d.base.model.PolyCylinder;

/**
 * Interface that defines the operations performed on a visualization state
 * 
 * @author David Montaño
 * 
 */
public interface IVisualizationState {

	void initialize();

	Iterable<Container> containersInView();

	void updateContainersInView(List<Container> containersToAdd, List<Container> containersToDelete);

	boolean hasContainersSelected();

	Iterable<PolyCylinder> selectedPolycylinders();

	void addContainerToViewWithoutValidation(Container container);

	Iterable<Container> selectedContainers();

	void deleteSelectedContainers();

	void clearContainers();

	boolean addContainerToSelection(Container container, boolean toggleContainerSelection);

	boolean addPolyCylinderToSelection(PolyCylinder polycylinder, boolean togglePolycylinderSelection);

	void clearSelectionOnPolycylinders();

	boolean hasMultiplePolyCylindersSelected();

	Container firstContainer();

	boolean hasContainersInView();

	Container getNextSelectableContainer();

	Container getPreviousSelectableContainer();

	void useNextLevelContainers();

	void usePreviousLevelContainers();

	void reset();

	void validatePolycylindersSelection();


}
