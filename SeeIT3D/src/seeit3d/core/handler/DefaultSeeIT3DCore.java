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

import static seeit3d.general.bus.EventBus.*;

import java.io.*;
import java.util.*;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import seeit3d.core.api.*;
import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.*;
import seeit3d.general.bus.utils.FunctionToApplyOnContainer;
import seeit3d.general.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.*;
import seeit3d.general.model.generator.metrics.MetricCalculator;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Class that handles the interactions with the model of SeeIT3D. It tracks the general state of the visualization system.
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultSeeIT3DCore implements ISeeIT3DCore, IEventListener {

	private final IVisualizationState state;

	private final ISceneGraphManipulator sceneGraphHandler;

	private boolean isSynchronzationWithPackageExplorerSet = false;

	private boolean isSelectionToolSet = false;

	private final Preferences preferences;

	@Inject
	public DefaultSeeIT3DCore(IVisualizationState state, ISceneGraphManipulator sceneGraphHandler) {
		this.state = state;
		this.sceneGraphHandler = sceneGraphHandler;
		preferences = Preferences.getInstance();

		registerListener(ToggleSynchronizationPackageExplorerVsViewEvent.class, this);
		registerListener(AddContainerEvent.class, this);
		registerListener(DeleteContainersEvent.class, this);
		registerListener(ChangeSelectionEvent.class, this);
		registerListener(KeyBasedChangeSelectionEvent.class, this);
		registerListener(ScaleContainerEvent.class, this);
		registerListener(ChangeGranularityLevelEvent.class, this);
		registerListener(ResetVisualizationEvent.class, this);
		registerListener(ChangeTransparencyEvent.class, this);
		registerListener(LoadVisualizationEvent.class, this);
		registerListener(SaveVisualizationEvent.class, this);
		registerListener(PerformOperationOnSelectedContainersEvent.class, this);
		registerListener(PerformOperationOnSelectedPolycylindersEvent.class, this);
		registerListener(RegisterPickingCallbackEvent.class, this);
		registerListener(UnregisterPickingCallbackEvent.class, this);
		registerListener(ColorScaleChangedEvent.class, this);
		registerListener(VisualizePolycylinderAsContainerEvent.class, this);
		registerListener(ActivateSelectionToolEvent.class, this);
		registerListener(SelectionToolEndedEvent.class, this);

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
			List<Container> containers = ((AddContainerEvent) event).getContainers();
			addContainersToView(containers);
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
			List<PolyCylinder> polycylinders = selectionChanged.getPolycylinders();
			List<Container> containers = selectionChanged.getContainers();
			changeSelectionAndUpdateMappingView(containers, polycylinders, toogleContainerSelection, tooglePolycylinderSelection);
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

		if (event instanceof LoadVisualizationEvent) {
			loadVisualization(((LoadVisualizationEvent) event).getInput());
		}

		if (event instanceof SaveVisualizationEvent) {
			saveVisualization(((SaveVisualizationEvent) event).getOutput());
		}

		if (event instanceof PerformOperationOnSelectedContainersEvent) {
			PerformOperationOnSelectedContainersEvent operation = (PerformOperationOnSelectedContainersEvent) event;
			operationOnSelectedContainers(operation);
		}

		if (event instanceof PerformOperationOnSelectedPolycylindersEvent) {
			PerformOperationOnSelectedPolycylindersEvent operation = (PerformOperationOnSelectedPolycylindersEvent) event;
			operationOnSelectedPolycylinders(operation);
		}

		if (event instanceof RegisterPickingCallbackEvent) {
			sceneGraphHandler.setupTranslationCallback(((RegisterPickingCallbackEvent) event).getCallback());
		}

		if (event instanceof UnregisterPickingCallbackEvent) {
			sceneGraphHandler.unregisterPickingCallback(((UnregisterPickingCallbackEvent) event).getCallback());
		}

		if (event instanceof ColorScaleChangedEvent) {
			refreshVisualization();
		}

		if (event instanceof VisualizePolycylinderAsContainerEvent) {
			visualizePolycylinderAsContainer();
		}

		if (event instanceof ActivateSelectionToolEvent) {
			sceneGraphHandler.activateSelectionTool();
			isSelectionToolSet = true;
			updateOrbitingState();
		}

		if (event instanceof SelectionToolEndedEvent) {
			isSelectionToolSet = false;
			updateOrbitingState();
		}

	}

	private void updateOrbitingState() {
		boolean enabled = !isSelectionToolSet && !state.hasContainersSelected();
		sceneGraphHandler.changeOrbitState(enabled);
	}

	private void visualizePolycylinderAsContainer() {
		List<Container> containersToAdd = new ArrayList<Container>();
		Iterable<PolyCylinder> polycylinders = state.selectedPolycylinders();

		for (PolyCylinder polycylinder : polycylinders) {
			Iterable<Container> containers = state.containersInView();
			for (Container container : containers) {
				if (container.hasPolycylinder(polycylinder)) {
					Container converted = container.toContainer(polycylinder);
					containersToAdd.add(converted);
				}
			}
		}
		addContainersToView(containersToAdd);
	}

	private void operationOnSelectedPolycylinders(PerformOperationOnSelectedPolycylindersEvent operation) {
		FunctionToApplyOnPolycylinders function = operation.getFunction();
		Utils.applyTransformation(state.selectedPolycylinders(), function);
		if (operation.isVisualizationNeedsRefresh()) {
			refreshVisualization();
		}
	}

	private void operationOnSelectedContainers(PerformOperationOnSelectedContainersEvent operation) {
		FunctionToApplyOnContainer function = operation.getFunction();
		Utils.applyTransformation(unmodifiableSelectedContainers(), function);
		if (operation.isVisualizationNeedsRefresh()) {
			refreshVisualization();
		}
	}

	/****************************************/
	/********** LAYOUT PROPERTIES *************/
	synchronized void doContainerLayout() {

		float currentXPosition = 0.0f;
		float currentZPosition = 0.0f;

		float maxX = -1f;

		int added = 0;
		for (Container container : state.containersInView()) {
			currentXPosition += container.getWidth();
			Vector3f newPosition = new Vector3f(currentXPosition, 0.0f, currentZPosition);
			added++;
			container.setPosition(newPosition);
			maxX = Math.max(maxX, currentXPosition);
			if (added % preferences.getContainersPerRow() == 0) {
				currentXPosition = 0.0f;
				currentZPosition += container.getDepth() * 2 + ViewConstants.CONTAINERS_SPACING;
			} else {
				currentXPosition += container.getWidth() + ViewConstants.CONTAINERS_SPACING;
			}
		}

		sceneGraphHandler.setViewersPosition(maxX);
		publishEvent(new ContainersLayoutDoneEvent());
	}

	/**************************************/
	/******* OPERATIONS ON VIEW PROPERTIES **/

	private void addContainersToView(List<Container> containers) {
		for (Container container : containers) {
			state.addContainerToViewWithoutValidation(container);
		}
		refreshVisualization();
	}

	private synchronized void deleteSelectedContainers() {
		for (Container container : state.selectedContainers()) {
			sceneGraphHandler.removeScene(container);
		}
		state.deleteSelectedContainers();
		refreshVisualization();
	}

	private synchronized void deleteAllContainers() {
		state.clearContainers();
		sceneGraphHandler.clearScene();
		refreshVisualization();
	}

	private synchronized void changeSelectionAndUpdateMappingView(List<Container> containers, List<PolyCylinder> polycylinders, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
		boolean mappingNeedsRefresh = false;
		synchronized (DefaultSeeIT3DCore.class) {

			boolean selectionContainerChanged = false;
			for (Container container : containers) {
				selectionContainerChanged |= state.addContainerToSelection(container, toggleContainerSelection);
			}

			boolean selectionNeedsRefresh = false;

			if (selectionContainerChanged) {
				selectionNeedsRefresh |= true;
				mappingNeedsRefresh = true;
			}

			if (state.hasContainersSelected()) {
				boolean selectionPolyCylinderChanged = false;
				for (PolyCylinder polycylinder : polycylinders) {
					selectionContainerChanged |= state.addPolyCylinderToSelection(polycylinder, togglePolycylinderSelection);
				}

				selectionNeedsRefresh |= selectionPolyCylinderChanged;
				triggerSyncronizationPackageExplorerVsViewEvent();
			} else {
				state.clearSelectionOnPolycylinders();
				selectionNeedsRefresh |= true;
			}

			if (selectionNeedsRefresh) {
				refreshSelection();
			}

			updateOrbitingState();
		}
		if (mappingNeedsRefresh) {
			updateMappingView();
		}
	}

	private void triggerSyncronizationPackageExplorerVsViewEvent() {
		if (isSynchronzationWithPackageExplorerSet) {
			publishEvent(new SynchronizePackageExplorerVsViewEvent(state.selectedPolycylinders()));
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
		publishEvent(event);
	}

	private synchronized void changeContainerSelection(boolean increase) {
		if (state.hasContainersInView()) {
			Container container = null;
			if (increase) {
				container = state.getNextSelectableContainer();
			} else {
				container = state.getPreviousSelectableContainer();
			}
			List<Container> containers = Lists.newArrayList(container);
			changeSelectionAndUpdateMappingView(containers, null, false, false);
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
		refreshVisualization();
	}

	private synchronized void updateMappingView() {
		IEvent event = new MappingViewNeedsUpdateEvent(unmodifiableSelectedContainers());
		publishEvent(event);
	}

	private synchronized void resetVisualization() {
		state.reset();
		refreshVisualization();
	}

	@SuppressWarnings("unchecked")
	private synchronized void loadVisualization(InputStream input) {
		try {
			ObjectInputStream in = new ObjectInputStream(input);
			List<Container> containers = (List<Container>) in.readObject();
			addContainersToView(containers);
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

	private synchronized void refreshVisualization() {
		refreshSelection();
		sceneGraphHandler.rebuildSceneGraph();
		doContainerLayout();
		updateMappingView();
	}

	private synchronized void refreshSelection() {

		for (Container container : state.selectedContainers()) {
			container.setSelected(true);
		}

		state.validatePolycylindersSelection();

		PolyCylinder lastSelectedPoly = null;
		Iterable<PolyCylinder> iteratorOnPolycylinders = state.selectedPolycylinders();
		for (PolyCylinder poly : iteratorOnPolycylinders) {
			poly.setSelected(true);
			lastSelectedPoly = poly;
		}

		updateCurrentSelectionValues(lastSelectedPoly);
	}

	private synchronized List<Container> unmodifiableSelectedContainers() {
		List<Container> selectedContainers = new ArrayList<Container>();
		for (Container container : state.selectedContainers()) {
			selectedContainers.add(container);
		}
		return Collections.unmodifiableList(selectedContainers);
	}



}
