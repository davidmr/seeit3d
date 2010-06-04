/**
 * Copyright (C) 2010  David Monta�o
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

import java.io.*;
import java.util.*;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import seeit3d.core.api.SeeIT3DCore;
import seeit3d.core.model.*;
import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.general.bus.*;
import seeit3d.general.bus.events.*;
import seeit3d.general.error.ErrorHandler;
import seeit3d.utils.ViewConstants;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.pickfast.behaviors.PickingCallback;

/**
 * Class that handles the interactions between the different parts of SeeIT3D. It tracks the general state of the visualization system.
 * 
 * @author David Monta�o
 * 
 */
@Deprecated
public class SeeIT3DManager implements SeeIT3DCore, IEventListener {

	// TODO handle operations on polycylinders as a function to apply them. This means that the interested on performing
	// an operation on the must trigger an event which includes the function to be applied as an implementation of an interface like IOperationOnPolycylinders
	private final VisualizationState state;

	private final SceneGraphHandler sceneGraphHandler;

	private boolean isSynchronzationWithPackageExplorerSet = false;

	private final Preferences preferences;

	public SeeIT3DManager() {
		state = new VisualizationState(this);
		sceneGraphHandler = new SceneGraphHandler(this);
		preferences = Preferences.getInstance();

		EventBus.registerListener(ToggleSynchronizationPackageExplorerVsViewEvent.class, this);
		EventBus.registerListener(AddContainerEvent.class, this);
		EventBus.registerListener(MappingChangedEvent.class, this);
		EventBus.registerListener(RemoveMetricEvent.class, this);
		EventBus.registerListener(DeleteContainersEvent.class, this);
		EventBus.registerListener(ChangeSelectionEvent.class, this);
		EventBus.registerListener(KeyBasedChangeSelectionEvent.class, this);
		EventBus.registerListener(ScaleContainerEvent.class, this);
		EventBus.registerListener(ChangeGranularityLevelEvent.class, this);
		EventBus.registerListener(ResetVisualizationEvent.class, this);
		EventBus.registerListener(ChangeTransparencyEvent.class, this);
		EventBus.registerListener(SortPolycylindersEvent.class, this);
		EventBus.registerListener(LoadVisualizationEvent.class, this);
		EventBus.registerListener(SaveVisualizationEvent.class, this);
		EventBus.registerListener(ChangeSortingVisualPropertyEvent.class, this);

	}

	/****************************************/
	/******* EVENT PROCESSOR ******************/
	@Override
	public void processEvent(IEvent event) {
		if (event instanceof ToggleSynchronizationPackageExplorerVsViewEvent) {
			isSynchronzationWithPackageExplorerSet = !isSynchronzationWithPackageExplorerSet;
			triggerSyncronizationPackageExplorerVsViewEvent();
		}
		if (event instanceof AddContainerEvent) {
			Container container = ((AddContainerEvent) event).getContainer();
			state.addContainerToView(container);
		}

		if (event instanceof MappingChangedEvent) {
			MappingChangedEvent mappingChanged = (MappingChangedEvent) event;
			VisualProperty visualProp = mappingChanged.getVisualProperty();
			MetricCalculator metric = mappingChanged.getMetricCalculator();
			updateSelectedContainersMapping(metric, visualProp);
		}

		if (event instanceof RemoveMetricEvent) {
			MetricCalculator metric = ((RemoveMetricEvent) event).getMetric();
			removeSelectContainersMapping(metric);
		}

		if (event instanceof DeleteContainersEvent) {
			boolean all = ((DeleteContainersEvent) event).isAll();
			if (all) {
				deleteAllContainers();
			} else {
				deleteSelectedContainers();
			}
		}

		if (event instanceof ChangeSelectionEvent) {
			ChangeSelectionEvent selectionChanged = (ChangeSelectionEvent) event;
			boolean tooglePolycylinderSelection = selectionChanged.isTogglePolycylinderSelection();
			boolean toogleContainerSelection = selectionChanged.isToggleContainerSelection();
			PolyCylinder polycylinder = selectionChanged.getPolycylinder();
			Container container = selectionChanged.getContainer();
			changeSelectionAndUpdateMappingView(container, polycylinder, toogleContainerSelection, tooglePolycylinderSelection);
		}

		if (event instanceof KeyBasedChangeSelectionEvent) {
			changeContainerSelection(((KeyBasedChangeSelectionEvent) event).isIncrease());
		}

		if (event instanceof ScaleContainerEvent) {
			scaleCurrentContainer(((ScaleContainerEvent) event).isUp());
		}

		if (event instanceof ChangeGranularityLevelEvent) {
			updateViewUsingLevelOnSelectedContainer(((ChangeGranularityLevelEvent) event).isFiner());
		}

		if (event instanceof ResetVisualizationEvent) {
			resetVisualization();
		}

		if (event instanceof ChangeTransparencyEvent) {
			changeTransparencyPolyCylindersSelection(((ChangeTransparencyEvent) event).isMoreTransparent());
		}

		if (event instanceof SortPolycylindersEvent) {
			sortPolyCylinders();
		}

		if (event instanceof LoadVisualizationEvent) {
			loadVisualization(((LoadVisualizationEvent) event).getInput());
		}

		if (event instanceof SaveVisualizationEvent) {
			saveVisualization(((SaveVisualizationEvent) event).getOutput());
		}

		if (event instanceof ChangeSortingVisualPropertyEvent) {
			state.setSortingProperty(((ChangeSortingVisualPropertyEvent) event).getVisualProperty());
		}


	}

	/**************************************/
	/********* OPERATIONS TO SCENE GRAPH ***/
	synchronized void setupTranslationCallback(PickingCallback callback) {
		sceneGraphHandler.setupTranslationCallback(callback);
	}

	@Override
	public synchronized SeeIT3DCanvas getMainCanvas() {
		return sceneGraphHandler.getCanvas();
	}

	/****************************************/
	/********** LAYOUT PROPERTIES *************/
	synchronized void doContainerLayout() {

		float currentXPosition = 0.0f;
		float currentZPosition = 0.0f;

		float maxX = Float.MIN_VALUE;

		int added = 0;
		for (Container container : state.containersInView()) {

			currentXPosition += container.getWidth();

			Vector3f newPosition = new Vector3f(currentXPosition, 0.0f, currentZPosition);
			added++;

			container.setPosition(newPosition);

			if (added % preferences.getContainersPerRow() == 0) {
				currentXPosition = 0.0f;
				currentZPosition += container.getDepth() * 2 + ViewConstants.CONTAINERS_SPACING;
			} else {
				currentXPosition += container.getWidth() + ViewConstants.CONTAINERS_SPACING;
			}

			maxX = Math.max(maxX, currentXPosition);

		}

		sceneGraphHandler.setViewersPosition(maxX);
		EventBus.publishEvent(new ContainersLayoutDoneEvent());

	}

	/**************************************/
	/******* OPERATIONS ON VIEW PROPERTIES **/

	private synchronized void updateSelectedContainersMapping(MetricCalculator metric, VisualProperty visualProp) {
		for (Container container : state.selectedContainers()) {
			container.updateMapping(metric, visualProp);
		}
		refreshVisualization();
	}

	private void removeSelectContainersMapping(MetricCalculator metric) {
		for (Container container : state.selectedContainers()) {
			container.removeFromMapping(metric);
		}
		refreshVisualization();

	}

	private synchronized void deleteSelectedContainers() {
		for (Container container : state.selectedContainers()) {
			sceneGraphHandler.removeScene(container);
		}
		state.deleteSelectedContainers();
	}

	private synchronized void deleteAllContainers() {
		state.clearContainers();
		sceneGraphHandler.clearScene();
		updateMappingView();
	}

	private synchronized void changeSelectionAndUpdateMappingView(Container newContainer, PolyCylinder polycylinder, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
		boolean mappingNeedsRefresh = false;
		synchronized (SeeIT3DManager.class) {
			sceneGraphHandler.disableOrbiting();

			boolean selectionContainerChanged = state.addContainerToSelection(newContainer, toggleContainerSelection);
			boolean selectionNeedsRefresh = false;

			if (selectionContainerChanged) {
				selectionNeedsRefresh |= true;
				mappingNeedsRefresh = true;
			}

			if (state.hasContainersSelected()) {
				boolean selectionPolyCylinderChanged = state.addPolyCylinderToSelection(polycylinder, togglePolycylinderSelection);
				selectionNeedsRefresh |= selectionPolyCylinderChanged;
				triggerSyncronizationPackageExplorerVsViewEvent();
			} else {
				state.clearSelectionOnPolycylinders();
				selectionNeedsRefresh |= true;
			}

			if (!state.hasContainersSelected()) {
				sceneGraphHandler.enableOrbiting();
			}

			if (selectionNeedsRefresh) {
				refreshSelection();
			}
		}
		if (mappingNeedsRefresh) {
			updateMappingView();
		}
	}

	private void triggerSyncronizationPackageExplorerVsViewEvent() {
		if (isSynchronzationWithPackageExplorerSet) {
			EventBus.publishEvent(new SynchronizePackageExplorerVsViewEvent(state.iteratorOnSelectedPolycylinders()));
		}
	}

	private synchronized void updateCurrentSelectionValues(PolyCylinder poly) {
		boolean hasMultipleSelection = state.hasMultiplePolyCylindersSelected();
		Map<String, String> currentMetricsValuesFromSelection = new HashMap<String, String>();

		if (hasMultipleSelection) {
			currentMetricsValuesFromSelection.put("Multiple selection", "-");
		} else {
			if (poly != null) {
				if (state.hasContainersSelected()) {
					Container selectedContainer = state.firstContainer();
					Map<MetricCalculator, VisualProperty> propertiesMapAccordingToLevel = selectedContainer.getPropertiesMap();
					for (Map.Entry<MetricCalculator, VisualProperty> entry : propertiesMapAccordingToLevel.entrySet()) {
						VisualProperty visualProperty = entry.getValue();
						VisualPropertyValue propertyValue = poly.getVisualProperty(visualProperty);
						String value = propertyValue.getRealValue();
						currentMetricsValuesFromSelection.put(entry.getKey().getMetricName(), value);
					}
				}
			}
		}
		IEvent event = new SelectedInformationChangedEvent(state.selectedContainers(), currentMetricsValuesFromSelection);
		EventBus.publishEvent(event);
	}

	private synchronized void changeContainerSelection(boolean increase) {
		if (state.hasContainersInView()) {
			Container container = null;
			if (increase) {
				container = state.getNextSelectableContainer();
			} else {
				container = state.getPreviousSelectableContainer();
			}

			changeSelectionAndUpdateMappingView(container, null, false, false);
		}
	}

	private synchronized void scaleCurrentContainer(boolean scaleUp) {
		for (Container container : state.selectedContainers()) {
			TransformGroup transformGroup = container.getTransformGroup();
			Transform3D transform = new Transform3D();
			transformGroup.getTransform(transform);
			double scaleStep = preferences.getScaleStep();
			if (scaleUp) {
				transform.setScale(transform.getScale() + scaleStep);
			} else {
				transform.setScale(transform.getScale() - scaleStep);
			}
			transformGroup.setTransform(transform);
		}
	}

	private void updateViewUsingLevelOnSelectedContainer(boolean finerLevel) {
		if (finerLevel) {
			state.useNextLevelContainers();
		} else {
			state.usePreviousLevelContainers();
		}
	}

	private synchronized void updateMappingView() {
		IEvent event = new MappingViewNeedsUpdateEvent(getCurrentSelectedContainers());
		EventBus.publishEvent(event);
	}

	private synchronized void resetVisualization() {
		state.reset();
		sceneGraphHandler.rebuildSceneGraph();
		doContainerLayout();
	}

	private synchronized void changeTransparencyPolyCylindersSelection(boolean moreTransparent) {
		Iterator<PolyCylinder> iterator = state.iteratorOnSelectedPolycylinders();
		while (iterator.hasNext()) {
			PolyCylinder poly = iterator.next();
			poly.changeTransparency(moreTransparent);
		}
	}

	private synchronized void sortPolyCylinders() {
		for (Container container : state.selectedContainers()) {
			sceneGraphHandler.removeScene(container);
			container.setSortingProperty(state.getSortingProperty());
			container.setSorted(true);
			container.updateVisualRepresentation();
			refreshVisualization();
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized void loadVisualization(InputStream input) {
		try {
			ObjectInputStream in = new ObjectInputStream(input);
			List<Container> containers = (List<Container>) in.readObject();
			for (Container container : containers) {
				state.addContainerToView(container);
			}
			refreshVisualization();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while reading visualization file. Possibly wrong format or type");
			e.printStackTrace();
		}
	}

	private synchronized void saveVisualization(OutputStream output) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(output);
			List<Container> allContainers = new ArrayList<Container>();
			for (Container container : state.containersInView()) {
				allContainers.add(container);
			}
			out.writeObject(allContainers);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************************/
	/**** STATE UPDATES ***/

	@Override
	public synchronized void refreshVisualization() {
		refreshSelection();
		sceneGraphHandler.rebuildSceneGraph();
		doContainerLayout();
		updateMappingView();
	}

	private synchronized void refreshSelection() {

		for (Container container : state.selectedContainers()) {
			container.setSelected(true);
		}

		validatePolycylindersSelection();

		PolyCylinder lastSelectedPoly = null;
		Iterator<PolyCylinder> iteratorOnPolycylinders = state.iteratorOnSelectedPolycylinders();
		while (iteratorOnPolycylinders.hasNext()) {
			PolyCylinder poly = iteratorOnPolycylinders.next();
			poly.setSelected(true);
			lastSelectedPoly = poly;
		}

		updateCurrentSelectionValues(lastSelectedPoly);
	}

	private synchronized void validatePolycylindersSelection() {
		Iterator<PolyCylinder> iteratorOnPolycylinders = state.iteratorOnSelectedPolycylinders();
		while (iteratorOnPolycylinders.hasNext()) {
			PolyCylinder poly = iteratorOnPolycylinders.next();
			if (!isPolyCylinderInSelection(poly)) {
				poly.setSelected(false);
				iteratorOnPolycylinders.remove();
			}
		}
	}

	private boolean isPolyCylinderInSelection(PolyCylinder poly) {
		for (Container container : state.selectedContainers()) {
			if (container.hasPolycylinder(poly)) {
				return true;
			}
		}
		return false;
	}

	synchronized void addContainerToViewWithoutValidation(Container container) {
		state.addContainerToViewWithoutValidation(container);
	}

	void addContainerToViewWithoutValidation(List<Container> containers) {
		for (Container container : containers) {
			addContainerToViewWithoutValidation(container);
		}
	}

	@Override
	public Iterable<Container> containersInView() {
		return state.containersInView();
	}

	@Override
	public synchronized List<Container> getCurrentSelectedContainers() {
		List<Container> selectedContainers = new ArrayList<Container>();
		for (Container container : state.selectedContainers()) {
			selectedContainers.add(container);
		}
		return Collections.unmodifiableList(selectedContainers);
	}

	@Override
	public synchronized VisualProperty getCurrentSortingProperty() {
		return state.getSortingProperty();
	}

	@Override
	public synchronized String getCurrentSelectedContainersAsString() {

		List<String> names = new ArrayList<String>();

		for (Container container : state.selectedContainers()) {
			names.add(container.getName());
		}

		if (names.isEmpty()) {
			names.add("None Selected");
		}

		return names.toString();
	}

	@Override
	public synchronized void useSceneGraphRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> sceneGraphRelationshipGenerator) {
		state.useScenGraphRelationshipGeneratorOnSelectedContainers(sceneGraphRelationshipGenerator);
	}

	@Override
	public synchronized void setShowRelatedContainers(boolean showRelated) {
		sceneGraphHandler.setShowRelatedContainers(showRelated);
	}

	@Override
	public synchronized boolean isShowRelatedContainers() {
		return sceneGraphHandler.isShowRelatedContainers();
	}

}
