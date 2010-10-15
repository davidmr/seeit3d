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
package seeit3d.core.handler;

import java.util.*;

import seeit3d.core.api.IVisualizationState;
import seeit3d.core.api.IVisualizationStateChecker;
import seeit3d.core.handler.utils.ContainersSelectedIterator;
import seeit3d.core.handler.utils.PolycylindersSelectedIterator;
import seeit3d.general.error.exception.IllegalVisualizationStateException;
import seeit3d.general.model.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class keeps track of the visualization state, like the selected containers in the view, checks of visualization state and sorting property
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultVisualizationState implements IVisualizationState {

	private final List<Container> containersInView;

	private VisualProperty sortingProperty = VisualProperty.HEIGHT;

	private final IVisualizationStateChecker stateChecker;

	@Inject
	public DefaultVisualizationState(IVisualizationStateChecker stateChecker) {
		containersInView = new ArrayList<Container>();
		this.stateChecker = stateChecker;
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

	// void addContainerToView(Container container) {
	// if (container == null) {
	// throw new NullPointerException("Container can not be null");
	// }
	// try {
	// List<Container> newContainers = new ArrayList<Container>();
	// newContainers.add(container);
	// newContainers.addAll(containersInView);
	//
	// validateState(newContainers);
	//
	// if (containersInView.contains(container)) {
	// container = container.createCopy();
	// }
	// containersInView.add(container);
	// container.setSelected(true);
	// viewNeedUpdate();
	// } catch (IllegalVisualizationStateException ex) {
	// handleIllegalVisualizationState(ex);
	// }
	// }

	// TODO handle with validation
	@Override
	public void addContainerToViewWithoutValidation(Container container) {
		if (container != null && !containersInView.contains(container)) {
			containersInView.add(container);
		}
	}

	public void deleteContainerFromView(Container container) {
		containersInView.remove(container);
	}

	@Override
	public void clearContainers() {
		containersInView.clear();
	}

	@Override
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

	@Override
	public boolean hasContainersInView() {
		return containersInView.iterator().hasNext();
	}

	@Override
	public boolean hasContainersSelected() {
		return new ContainersSelectedIterator(containersInView).hasNext();
	}

	void clearSelection() {
		for (Container container : containersInView) {
			container.setSelected(false);
		}
	}

	@Override
	public Iterable<Container> selectedContainers() {
		return new Iterable<Container>() {
			@Override
			public Iterator<Container> iterator() {
				return new ContainersSelectedIterator(containersInView);
			}
		};
	}

	@Override
	public Iterable<Container> containersInView() {
		return new Iterable<Container>() {
			@Override
			public Iterator<Container> iterator() {
				return containersInView.iterator();
			}
		};
	}

	@Override
	public Container firstContainer() {
		return new ContainersSelectedIterator(containersInView).next();
	}

	@Override
	public void deleteSelectedContainers() {
		ContainersSelectedIterator iterator = new ContainersSelectedIterator(containersInView);
		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	@Override
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

	@Override
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

	@Override
	public void useNextLevelContainers() {
		List<Container> newContainers = new ArrayList<Container>();
		List<Container> oldContainers = new ArrayList<Container>();
		for (Container container : selectedContainers()) {
			Container nextLevelContainer = container.buildContainerForNextLevel();
			nextLevelContainer.setSelected(true);
			newContainers.add(nextLevelContainer);
			container.setSelected(false);
			oldContainers.add(container);
		}
		try {
			validateState(newContainers);
			containersInView.removeAll(oldContainers);
			containersInView.addAll(newContainers);
		} catch (IllegalVisualizationStateException ex) {
			handleIllegalVisualizationState(ex);
		}
	}

	@Override
	public void usePreviousLevelContainers() {
		List<Container> newContainers = new ArrayList<Container>();
		List<Container> oldContainers = new ArrayList<Container>();
		for (Container container : selectedContainers()) {
			Container previousLevel = container.buildContainerForPreviousLevel();
			previousLevel.setSelected(true);
			newContainers.add(previousLevel);
			container.setSelected(false);
			oldContainers.add(container);
		}
		containersInView.removeAll(oldContainers);
		containersInView.addAll(newContainers);
	}

	@Override
	public void updateContainersInView(List<Container> containersToAdd, List<Container> containersToDelete) {
		for (Container container : containersToAdd) {
			addContainerToViewWithoutValidation(container);
		}

		for (Container container : containersToDelete) {
			deleteContainerFromView(container);
		}
	}

	/***********************************/
	/*********** POLYCYLINDERS **********/

	@Override
	public boolean addPolyCylinderToSelection(PolyCylinder polycylinder, boolean toggleInSelection) {
		boolean success = false;
		if (polycylinder != null) {
			if (polycylinder.isSelected()) {
				if (toggleInSelection) {
					polycylinder.setSelected(false);
					success = true;
				}
			} else {
				if (toggleInSelection) {
					polycylinder.setSelected(true);
				} else {
					clearSelectionOnPolycylinders();
					polycylinder.setSelected(true);
				}
				success = true;
			}
		} else {
			if (!hasPolycylinderSelected()) {
				clearSelectionOnPolycylinders();
				success = true;
			}
		}
		return success;

	}

	private Iterator<PolyCylinder> iteratorOnSelectedPolycylinders() {
		List<PolyCylinder> polycylinders = new ArrayList<PolyCylinder>();
		for (Container container : containersInView) {
			polycylinders.addAll(container.getPolycylinders());
		}
		return new PolycylindersSelectedIterator(polycylinders);
	}

	private boolean hasPolycylinderSelected() {
		Iterator<PolyCylinder> iterator = iteratorOnSelectedPolycylinders();
		return iterator.hasNext();
	}

	@Override
	public void clearSelectionOnPolycylinders() {
		Iterator<PolyCylinder> iterator = iteratorOnSelectedPolycylinders();
		while (iterator.hasNext()) {
			PolyCylinder polyCylinder = iterator.next();
			polyCylinder.setSelected(false);
		}
	}

	@Override
	public Iterable<PolyCylinder> selectedPolycylinders() {
		return new Iterable<PolyCylinder>() {
			@Override
			public Iterator<PolyCylinder> iterator() {
				return iteratorOnSelectedPolycylinders();
			}
		};
	}

	@Override
	public boolean hasMultiplePolyCylindersSelected() {
		Iterator<PolyCylinder> iterator = iteratorOnSelectedPolycylinders();
		if (iterator.hasNext()) {
			iterator.next();
			return iterator.hasNext();
		}
		return false;
	}

	/*************************/
	/**** OTHER OPERATIONS ****/

	@Override
	public void validatePolycylindersSelection() {
		Iterator<PolyCylinder> iteratorOnPolycylinders = iteratorOnSelectedPolycylinders();
		while (iteratorOnPolycylinders.hasNext()) {
			PolyCylinder poly = iteratorOnPolycylinders.next();
			if (!isPolyCylinderInSelection(poly)) {
				poly.setSelected(false);
			}
		}
	}

	private boolean isPolyCylinderInSelection(PolyCylinder poly) {
		for (Container container : selectedContainers()) {
			if (container.hasPolycylinder(poly)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		for (Container container : containersInView) {
			container.clearTransparencies();
		}
	}

	void setSortingProperty(VisualProperty sortingProperty) {
		this.sortingProperty = sortingProperty;
	}

	VisualProperty getSortingProperty() {
		return sortingProperty;
	}

}
