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
package seeit3d.internal.base.core.api;

import java.util.List;

import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;

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
