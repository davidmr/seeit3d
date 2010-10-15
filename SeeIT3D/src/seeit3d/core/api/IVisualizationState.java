package seeit3d.core.api;

import java.util.List;

import seeit3d.general.model.Container;
import seeit3d.general.model.PolyCylinder;

public interface IVisualizationState {

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
