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
package seeit3d.internal.base.core.handler;

import static seeit3d.internal.base.bus.EventBus.publishEvent;
import static seeit3d.internal.base.bus.EventBus.registerListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.ActivateSelectionToolEvent;
import seeit3d.internal.base.bus.events.AddContainerEvent;
import seeit3d.internal.base.bus.events.ChangeGranularityLevelEvent;
import seeit3d.internal.base.bus.events.ChangeSelectionEvent;
import seeit3d.internal.base.bus.events.ChangeTransparencyEvent;
import seeit3d.internal.base.bus.events.ColorScaleChangedEvent;
import seeit3d.internal.base.bus.events.ContainersLayoutDoneEvent;
import seeit3d.internal.base.bus.events.DeleteContainersEvent;
import seeit3d.internal.base.bus.events.KeyBasedChangeSelectionEvent;
import seeit3d.internal.base.bus.events.LoadVisualizationEvent;
import seeit3d.internal.base.bus.events.MappingViewNeedsUpdateEvent;
import seeit3d.internal.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.internal.base.bus.events.PerformOperationOnSelectedPolycylindersEvent;
import seeit3d.internal.base.bus.events.RegisterPickingCallbackEvent;
import seeit3d.internal.base.bus.events.ResetVisualizationEvent;
import seeit3d.internal.base.bus.events.SaveVisualizationEvent;
import seeit3d.internal.base.bus.events.ScaleContainerEvent;
import seeit3d.internal.base.bus.events.SelectedInformationChangedEvent;
import seeit3d.internal.base.bus.events.SelectionToolEndedEvent;
import seeit3d.internal.base.bus.events.SynchronizePackageExplorerVsViewEvent;
import seeit3d.internal.base.bus.events.ToggleSynchronizationPackageExplorerVsViewEvent;
import seeit3d.internal.base.bus.events.UnregisterPickingCallbackEvent;
import seeit3d.internal.base.bus.events.VisualizePolycylinderAsContainerEvent;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnPolycylinders;
import seeit3d.internal.base.core.api.ISceneGraphManipulator;
import seeit3d.internal.base.core.api.ISeeIT3DCore;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.core.api.IVisualizationState;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;
import seeit3d.internal.utils.Utils;
import seeit3d.internal.utils.ViewConstants;

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

	private final ISeeIT3DPreferences preferences;

	private boolean isSynchronzationWithPackageExplorerSet = false;

	private boolean isSelectionToolSet = false;

	@Inject
	public DefaultSeeIT3DCore(IVisualizationState state, ISceneGraphManipulator sceneGraphHandler, ISeeIT3DPreferences preferences) {
		this.state = state;
		this.sceneGraphHandler = sceneGraphHandler;
		this.preferences = preferences;

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

	@Override
	public void initialize() {
		sceneGraphHandler.initialize();
		state.initialize();
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
		Utils.applyFunction(state.selectedPolycylinders(), function);
		if (operation.isVisualizationNeedsRefresh()) {
			refreshVisualization();
		}
	}

	private void operationOnSelectedContainers(PerformOperationOnSelectedContainersEvent operation) {
		FunctionToApplyOnContainer function = operation.getFunction();
		Utils.applyFunction(unmodifiableSelectedContainers(), function);
		if (operation.isVisualizationNeedsRefresh()) {
			refreshVisualization();
		}
	}

	/****************************************/
	/********** LAYOUT PROPERTIES *************/
	void doContainerLayout() {

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

	private void deleteSelectedContainers() {
		for (Container container : state.selectedContainers()) {
			sceneGraphHandler.removeScene(container);
		}
		state.deleteSelectedContainers();
		refreshVisualization();
	}

	private void deleteAllContainers() {
		state.clearContainers();
		sceneGraphHandler.clearScene();
		refreshVisualization();
	}

	private void changeSelectionAndUpdateMappingView(List<Container> containers, List<PolyCylinder> polycylinders, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
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
					selectionPolyCylinderChanged |= state.addPolyCylinderToSelection(polycylinder, togglePolycylinderSelection);
				}

				selectionNeedsRefresh |= selectionPolyCylinderChanged;
				triggerSyncronizationPackageExplorerVsViewEvent();
			} else {
				state.clearSelectionOnPolycylinders();
				selectionNeedsRefresh |= true;
			}

			if (selectionNeedsRefresh) {
				refreshSelection();
				updateCurrentSelectionValues();
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

	private void updateCurrentSelectionValues() {
		IEvent event = new SelectedInformationChangedEvent(state.selectedContainers(), state.selectedPolycylinders());
		publishEvent(event);
	}

	private void changeContainerSelection(boolean increase) {
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

	private void scaleCurrentContainer(boolean scaleUp) {
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

	private void updateMappingView() {
		IEvent event = new MappingViewNeedsUpdateEvent(unmodifiableSelectedContainers());
		publishEvent(event);
	}

	private void resetVisualization() {
		state.reset();
		refreshVisualization();
	}

	@SuppressWarnings("unchecked")
	private void loadVisualization(InputStream input) {
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

	private void saveVisualization(OutputStream output) {
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

	private void refreshVisualization() {
		refreshSelection();
		sceneGraphHandler.rebuildSceneGraph();
		doContainerLayout();
		updateMappingView();
	}

	private void refreshSelection() {

		for (Container container : state.selectedContainers()) {
			container.setSelected(true);
		}

		state.validatePolycylindersSelection();

		Iterable<PolyCylinder> iteratorOnPolycylinders = state.selectedPolycylinders();
		for (PolyCylinder poly : iteratorOnPolycylinders) {
			poly.setSelected(true);
		}
	}

	private List<Container> unmodifiableSelectedContainers() {
		List<Container> selectedContainers = new ArrayList<Container>();
		for (Container container : state.selectedContainers()) {
			selectedContainers.add(container);
		}
		return Collections.unmodifiableList(selectedContainers);
	}

	@Override
	public SeeIT3DCanvas getCanvas() {
		return sceneGraphHandler.getCanvas();
	}

}
