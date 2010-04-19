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

import java.io.*;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.colorscale.IColorScale;
import seeit3d.colorscale.imp.ColdToHotColorScale;
import seeit3d.error.ErrorHandler;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.model.EclipseResourceRepresentation;
import seeit3d.model.representation.*;
import seeit3d.preferences.IPreferencesListener;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;
import seeit3d.view.SeeIT3DCanvas;
import seeit3d.view.listeners.LabelInformation;

/**
 * Class that handles the interactions between the different parts of SeeIT3D. It tracks the general state of the visualization system.
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DManager implements IPreferencesListener {

	/**
	 * Singleton
	 */
	private static final SeeIT3DManager manager;

	static {
		manager = new SeeIT3DManager();
	}

	public static SeeIT3DManager getInstance() {
		return manager;
	}

	private final VisualizationState state;

	private final SceneGraphHandler sceneGraphHandler;

	private IColorScale colorScale;

	private IMappingView mappingView = null;

	private boolean isSynchronzationWithPackageExplorerSet = false;

	private SelectionInformationAware selectionInformatioAware;

	private double scaleStep;

	private int containersPerRow;

	private int polycylindersPerRow;

	private Color3f highlightColor;

	private Color3f relationMarkColor;

	private float transparencyStep;

	public SeeIT3DManager() {
		sceneGraphHandler = new SceneGraphHandler(this);
		state = new VisualizationState(this);
		colorScale = new ColdToHotColorScale();
	}

	/**************************************/
	/******* OPERATIONS ON VIEW PROPERTIES **/
	public synchronized void registerMappingView(IMappingView newMappingView) {
		mappingView = newMappingView;
	}

	public void registerSelectionInformatioAware(LabelInformation selectionInformatioAware) {
		this.selectionInformatioAware = selectionInformatioAware;
	}

	public synchronized void addContainerToView(Container container) {
		state.addContainerToView(container);
	}

	public synchronized void doContainerLayout() {
		Iterator<Container> iterator = manager.iteratorOnAllContainers();
	
		float currentXPosition = 0.0f;
		float currentZPosition = 0.0f;
	
		float maxX = Float.MIN_VALUE;
	
		int added = 0;
		Container container = null;
		while (iterator.hasNext()) {
			container = iterator.next();
			added++;
			Vector3f newPosition = new Vector3f(currentXPosition + container.getWidth() / 2, 0.0f, currentZPosition + container.getDepth() / 2);
	
			if (added % containersPerRow == 0) {
				currentZPosition += container.getDepth() + ViewConstants.CONTAINERS_SPACING;
				currentXPosition = 0.0f;
			} else {
				currentXPosition += container.getWidth() + ViewConstants.CONTAINERS_SPACING;
			}
			maxX = Math.max(maxX, currentXPosition + container.getWidth());
	
			container.setPosition(newPosition);
		}
	
		sceneGraphHandler.setViewersPosition(maxX);
	
	}

	public synchronized void clearContainers() {
		state.clearContainers();
	}

	public synchronized void updateSelectedContainersMapping(BaseMetricCalculator metric, VisualProperty visualProp) {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			container.updateMapping(metric, visualProp);
		}
		refreshVisualization();
	}

	public void removeSelectContainersMapping(BaseMetricCalculator metric) {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			container.removeFromMapping(metric);
		}
		refreshVisualization();

	}

	public synchronized void updateCurrentSelectedContainer() {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			sceneGraphHandler.removeFromSceneGraph(container);
			container.updateVisualRepresentation();
			container.setSelected(true);
			refreshVisualization();
		}
		updateMappingView();
	}

	public synchronized void deleteSelectedContainers() {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			sceneGraphHandler.removeFromSceneGraph(container);
		}

		state.deleteSelectedContainers();

	}

	public synchronized void deleteAllContainers() {
		state.clearContainers();
		sceneGraphHandler.clearScene();
		updateMappingView();
	}

	public synchronized void changeSelectionAndUpdateMappingView(Container newContainer, PolyCylinder polycylinder, boolean toggleContainerSelection, boolean togglePolycylinderSelection) {
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
				if (isSynchronzationWithPackageExplorerSet) {
					activateNewSelection();
				}
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

	private synchronized void setSelectionToView(String viewId, ISelection newSelection) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
		view.getSite().getSelectionProvider().setSelection(newSelection);
	}

	public synchronized void showRelatedContainers() {

		Iterator<Container> iteratorAll = state.iteratorOnAllContainers();
		while (iteratorAll.hasNext()) {
			Container container = iteratorAll.next();
			container.deactivateRelationShipMark();
		}

		List<Container> relatedContainers = new ArrayList<Container>();

		Iterator<Container> iteratorSelected = state.iteratorOnSelectedContainers();
		while (iteratorSelected.hasNext()) {
			Container container = iteratorSelected.next();
			relatedContainers.addAll(container.getRelatedContainers());
			container.activateRelationShipMark();
		}

		for (Container relatedContainer : relatedContainers) {
			relatedContainer.activateRelationShipMark();
			state.addContainerToView(relatedContainer);
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
					Container selectedContainer = state.iteratorOnSelectedContainers().next();
					Map<BaseMetricCalculator, VisualProperty> propertiesMapAccordingToLevel = selectedContainer.getPropertiesMap();
					for (Map.Entry<BaseMetricCalculator, VisualProperty> entry : propertiesMapAccordingToLevel.entrySet()) {
						VisualProperty visualProperty = entry.getValue();
						VisualPropertyValue propertyValue = poly.getVisualProperty(visualProperty);
						String value = propertyValue.getRealValue();
						currentMetricsValuesFromSelection.put(entry.getKey().getMetricName(), value);
					}
				}
			}
		}
		selectionInformatioAware.updateInformation(state.iteratorOnSelectedContainers(), currentMetricsValuesFromSelection);
	}

	public synchronized void changeContainerSelection(boolean increase) {
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

	public synchronized void scaleCurrentContainer(boolean scaleUp) {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			BranchGroup containerBG = container.getContainerBG();
			TransformGroup transformGroup = (TransformGroup) containerBG.getChild(0);
			Transform3D transform = new Transform3D();
			transformGroup.getTransform(transform);
			if (scaleUp) {
				transform.setScale(transform.getScale() + scaleStep);
			} else {
				transform.setScale(transform.getScale() - scaleStep);
			}
			transformGroup.setTransform(transform);
		}
	}

	public void updateViewUsingLevelOnSelectedContainer(boolean nextLevel) {
		if (nextLevel) {
			state.useNextLevelContainers();
		} else {
			state.usePreviousLevelContainers();
		}
	}

	public synchronized void updateMappingView() {
		if (mappingView != null) {
			mappingView.updateMappingView(this);
		}
	}

	public synchronized void initializeVisualization(SeeIT3DCanvas canvas) {
		sceneGraphHandler.initializeVisualization(canvas);
	}

	public synchronized void resetVisualization() {
		state.reset();
		sceneGraphHandler.buildAllVisualization();
	}

	public synchronized void cleanVisualization() {
		state.clearContainers();
	}

	private synchronized void activateNewSelection() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean hasJava = false;
				List<Object> objectsInSelection = new ArrayList<Object>();
				Iterator<PolyCylinder> iterator = state.iteratorOnSelectedPolycylinders();
				while (iterator.hasNext()) {
					PolyCylinder poly = iterator.next();
					EclipseResourceRepresentation representation = poly.getRepresentation();
					objectsInSelection.add(representation.getAssociatedResource());
					if (representation.hasJavaElementRepresentation()) {
						objectsInSelection.add(representation.getAssociatedJavaElement());
						hasJava = true;
					}

				}
				ISelection newSelection = new StructuredSelection(objectsInSelection);
				setSelectionToView(Utils.NAVIGATOR_VIEW_ID, newSelection);
				if (hasJava) {
					setSelectionToView(Utils.ID_PACKAGE_EXPLORER, newSelection);
				}

			}
		});
	}

	public synchronized void changeTransparencyPolyCylindersSelection(boolean moreTransparent) {
		Iterator<PolyCylinder> iterator = state.iteratorOnSelectedPolycylinders();
		while (iterator.hasNext()) {
			PolyCylinder poly = iterator.next();
			poly.changeTransparency(moreTransparent);
		}
	}

	public synchronized void sortPolyCylinders() {
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			sceneGraphHandler.removeFromSceneGraph(container);
			container.setSortingProperty(state.getSortingProperty());
			container.setSorted(true);
			container.updateVisualRepresentation();
			refreshVisualization();
		}
	}

	/************************/
	/**** STATE UPDATES ***/

	public synchronized void toggleSynchronizationInPackageVsView() {
		isSynchronzationWithPackageExplorerSet = !isSynchronzationWithPackageExplorerSet;
		if (isSynchronzationWithPackageExplorerSet) {
			activateNewSelection();
		}
	}

	public synchronized void changeCurrentSortingPolyCylindersProperty(VisualProperty visualProperty) {
		state.setSortingProperty(visualProperty);
	}

	public synchronized void refreshVisualization() {
		refreshSelection();
		sceneGraphHandler.rebuildSceneGraph();
		updateMappingView();
	}

	private synchronized void refreshSelection() {

		Iterator<Container> iteratorOnSelected = state.iteratorOnSelectedContainers();
		while (iteratorOnSelected.hasNext()) {
			Container container = iteratorOnSelected.next();
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
		Iterator<Container> iteratorOnSelected = state.iteratorOnSelectedContainers();
		while (iteratorOnSelected.hasNext()) {
			Container container = iteratorOnSelected.next();
			if (container.hasPolycylinder(poly)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public synchronized void loadVisualization(InputStream input) throws IOException {
		ObjectInputStream in = new ObjectInputStream(input);
		try {
			List<Container> containers = (List<Container>) in.readObject();
			for (Container container : containers) {
				state.addContainerToView(container);
			}
			refreshVisualization();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveVisualization(OutputStream output) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(output);
		List<Container> allContainers = new ArrayList<Container>();
		Iterator<Container> iterator = iteratorOnAllContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			allContainers.add(container);
		}
		out.writeObject(allContainers);
		out.close();
	}

	public synchronized void openEditor(final PolyCylinder selectedPolyCylinder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				EclipseResourceRepresentation resource = selectedPolyCylinder.getRepresentation();
				IResource associatedResource = resource.getAssociatedResource();
				if (associatedResource != null) {
					IPath path = associatedResource.getFullPath();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					final IFile file = root.getFile(path);
					final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					final IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
	
					try {
						page.openEditor(new FileEditorInput(file), desc.getId());
					} catch (Exception e) {
						ErrorHandler.error("Error opening editor");
						e.printStackTrace();
					}
	
				} else {
					ErrorHandler.error("The selected polycylinder does not have an associated resource in the workspace");
				}
			}
		});
	}

	public Iterator<Container> iteratorOnAllContainers() {
		return state.iteratorOnAllContainers();
	}

	public synchronized List<Container> getCurrentSelectedContainers() {
		Iterator<Container> iteratorOnSelectedContainers = state.iteratorOnSelectedContainers();
		List<Container> selectedContainers = new ArrayList<Container>();
		while (iteratorOnSelectedContainers.hasNext()) {
			selectedContainers.add(iteratorOnSelectedContainers.next());
		}
		return Collections.unmodifiableList(selectedContainers);
	}

	public synchronized VisualProperty getCurrentSortingProperty() {
		return state.getSortingProperty();
	}

	public synchronized String getCurrentSelectedContainersAsString() {
	
		List<String> names = new ArrayList<String>();
	
		Iterator<Container> iterator = state.iteratorOnSelectedContainers();
		while (iterator.hasNext()) {
			Container container = iterator.next();
			names.add(container.getName());
		}
	
		if (names.isEmpty()) {
			names.add("None Selected");
		}
	
		return names.toString();
	}

	public synchronized IColorScale getColorScale() {
		return colorScale;
	}

	public synchronized int getPolycylindersPerRow() {
		return polycylindersPerRow;
	}

	public synchronized Color3f getHighlightColor() {
		return highlightColor;
	}

	public synchronized Color3f getRelationMarkColor() {
		return relationMarkColor;
	}

	public synchronized float getTransparencyStep() {
		return transparencyStep;
	}

	public synchronized void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	public synchronized SceneGraphHandler getSceneGraphHandler() {
		return sceneGraphHandler;
	}

	/**
	 * Listen preferences
	 */
	@Override
	public void scaleStepChanged(double newScale) {
		scaleStep = newScale;
	}

	@Override
	public synchronized void colorScaleChanged(IColorScale newColorScale) {
		this.colorScale = newColorScale;
	}

	@Override
	public synchronized void containersPerRowChanged(int containersPerRow) {
		this.containersPerRow = containersPerRow;
	}

	@Override
	public void backgroundColorChanged(Color3f newBackgroundColor) {
	}

	@Override
	public synchronized void relationMarkColorChanged(Color3f newRelationMarkColor) {
		relationMarkColor = newRelationMarkColor;
	}

	@Override
	public synchronized void highlightColorChanged(Color3f newHighlightColor) {
		highlightColor = newHighlightColor;
	}

	@Override
	public synchronized void polycylindersPerRowChanged(int newPolycylinderPerRow) {
		polycylindersPerRow = newPolycylinderPerRow;
	}

	@Override
	public synchronized void transparencyStepChanged(float transparencyStepChanged) {
		transparencyStep = transparencyStepChanged;
	}
}
