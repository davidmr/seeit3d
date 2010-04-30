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
package seeit3d.manager;

import java.util.*;

import seeit3d.error.exception.IllegalVisualizationStateException;
import seeit3d.model.representation.*;

/**
 * This class keeps track of the visualization state, like the selected containers in the view, checks of visualization state and sorting property
 * 
 * @author David Montaño
 * 
 */
public class VisualizationState {

	private final List<Container> containersInView;

	private final List<PolyCylinder> currentSelectionPolyCylinder;

	private VisualProperty sortingProperty = VisualProperty.HEIGHT;

	private final SeeIT3DManager manager;

	private final VisualizationStateChecker stateChecker;

	public VisualizationState(SeeIT3DManager manager) {
		this.manager = manager;
		containersInView = new ArrayList<Container>();
		currentSelectionPolyCylinder = new ArrayList<PolyCylinder>();
		stateChecker = new VisualizationStateChecker();
	}

	private void viewNeedUpdate() {
		manager.refreshVisualization();
	}

	private void validateState(List<Container> containers) throws IllegalVisualizationStateException {
		boolean validState = stateChecker.checkState(containers);
		if (!validState) {
			throw new IllegalVisualizationStateException("Visualization state not legal");
		}
	}

	private void handleIllegalVisualizationState(IllegalVisualizationStateException e) {
		reset();
	}

	/*************************/
	/****** CONTAINERS *******/

	public void addContainerToView(Container container) {
		if (container == null) {
			throw new NullPointerException("Container can not be null");
		}
		try {
			List<Container> newContainers = new ArrayList<Container>();
			newContainers.add(container);
			newContainers.addAll(containersInView);

			validateState(newContainers);

			if (containersInView.contains(container)) {
				container = container.createCopy();
			}
			containersInView.add(container);
			container.setSelected(true);
			container.updateVisualRepresentation();
			manager.doContainerLayout();
			viewNeedUpdate();
		} catch (IllegalVisualizationStateException ex) {
			handleIllegalVisualizationState(ex);
		}
	}

	protected void addContainerToViewWithoutValidation(Container container) {
		containersInView.add(container);
	}

	public void clearContainers() {
		containersInView.clear();
		viewNeedUpdate();
	}

	public boolean addContainerToSelection(Container newContainer, boolean toggleInSelection) {
		boolean succesful = false;
		if (newContainer != null) {
			if (newContainer.isSelected()) {
				if (toggleInSelection) {
					newContainer.setSelected(false);
					succesful = true;
				}
			} else {
				if (toggleInSelection) {
					newContainer.setSelected(true);
				} else {
					clearSelection();
					newContainer.setSelected(true);
				}
				succesful = true;
			}
		} else {
			if (hasContainersSelected()) {
				clearSelection();
				succesful = true;
			}
		}

		return succesful;
	}

	public boolean hasContainersInView() {
		return iteratorOnAllContainers().hasNext();
	}

	public boolean hasContainersSelected() {
		return iteratorOnSelectedContainers().hasNext();
	}

	private void clearSelection() {
		for (Container container : containersInView) {
			container.setSelected(false);
		}
	}

	public Iterator<Container> iteratorOnSelectedContainers() {
		return new ContainersSelectedIterator(containersInView);
	}

	public Iterator<Container> iteratorOnAllContainers() {
		return containersInView.iterator();
	}

	public void deleteSelectedContainers() {
		ContainersSelectedIterator iterator = new ContainersSelectedIterator(containersInView);
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
		viewNeedUpdate();
	}

	public Container getNextSelectableContainer() {
		int nextIndex = -1;
		for (int i = 0; i < containersInView.size(); i++) {
			Container container = containersInView.get(i);
			if (container.isSelected()) {
				if (i == containersInView.size() - 1) {
					nextIndex = 0;
				} else {
					nextIndex = (i + 1);
				}
			}
		}
		if (nextIndex == -1) {
			return containersInView.get(0);
		}
		return containersInView.get(nextIndex);

	}

	public Container getPreviousSelectableContainer() {
		int prevIndex = -1;
		for (int i = 0; i < containersInView.size(); i++) {
			Container container = containersInView.get(i);
			if (container.isSelected()) {
				if (i == 0) {
					prevIndex = containersInView.size() - 1;
				} else {
					prevIndex = i - 1;
				}
			}
		}
		if (prevIndex == -1) {
			return containersInView.get(0);
		}
		return containersInView.get(prevIndex);
	}

	public void useNextLevelContainers() {
		List<Container> newContainers = new ArrayList<Container>();
		List<Container> oldContainers = new ArrayList<Container>();
		Iterator<Container> containers = iteratorOnSelectedContainers();
		while (containers.hasNext()) {
			Container container = containers.next();
			container.setSelected(false);
			oldContainers.add(container);

			Container nextLevelContainer = container.buildContainerForNextLevel();
			nextLevelContainer.setSelected(true);
			newContainers.add(nextLevelContainer);

		}
		try {
			validateState(newContainers);
			containersInView.removeAll(oldContainers);
			containersInView.addAll(newContainers);
			viewNeedUpdate();
		} catch (IllegalVisualizationStateException ex) {
			handleIllegalVisualizationState(ex);
		}
	}

	public void usePreviousLevelContainers() {
		List<Container> newContainers = new ArrayList<Container>();
		Iterator<Container> containers = iteratorOnSelectedContainers();
		while (containers.hasNext()) {
			Container container = containers.next();
			Container previousLevelContainer = container.buildContainerForPreviousLevel();
			container.setSelected(false);
			previousLevelContainer.setSelected(true);
			newContainers.add(previousLevelContainer);
			containers.remove();
		}
		containersInView.addAll(newContainers);
		viewNeedUpdate();
	}

	/***********************************/
	/*********** POLYCYLINDERS **********/

	public boolean addPolyCylinderToSelection(PolyCylinder polycylinder, boolean toggleInSelection) {
		boolean success = false;
		if (polycylinder != null) {
			if (currentSelectionPolyCylinder.contains(polycylinder)) {
				if (toggleInSelection) {
					currentSelectionPolyCylinder.remove(polycylinder);
					polycylinder.setSelected(false);
					success = true;
				}
			} else {
				if (toggleInSelection) {
					currentSelectionPolyCylinder.add(polycylinder);
				} else {
					clearSelectionOnPolycylinders();
					currentSelectionPolyCylinder.add(polycylinder);
				}
				success = true;
			}
		} else {
			if (!currentSelectionPolyCylinder.isEmpty()) {
				clearSelectionOnPolycylinders();
				success = true;
			}
		}
		return success;
	}

	public void clearSelectionOnPolycylinders() {
		Iterator<PolyCylinder> iterator = currentSelectionPolyCylinder.iterator();
		while (iterator.hasNext()) {
			PolyCylinder polyCylinder = iterator.next();
			polyCylinder.setSelected(false);
			iterator.remove();
		}
	}

	public Iterator<PolyCylinder> iteratorOnSelectedPolycylinders() {
		return currentSelectionPolyCylinder.iterator();
	}

	public boolean hasMultiplePolyCylindersSelected() {
		return currentSelectionPolyCylinder.size() > 1;
	}

	/*************************/
	/**** OTHER OPERATIONS ****/

	public void reset() {
		Iterator<Container> iterator = iteratorOnAllContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			container.clearTransparencies();
		}
		viewNeedUpdate();
	}

	public void setSortingProperty(VisualProperty sortingProperty) {
		this.sortingProperty = sortingProperty;
	}

	public VisualProperty getSortingProperty() {
		return sortingProperty;
	}

}
