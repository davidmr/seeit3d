package seeit3d.manager;

import java.io.*;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Vector3f;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import seeit3d.colorscale.IColorScale;
import seeit3d.colorscale.imp.ColdToHotColorScale;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.model.EclipseResourceRepresentation;
import seeit3d.model.representation.*;
import seeit3d.preferences.Preferences;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;
import seeit3d.view.SeeIT3DCanvas;
import seeit3d.view.listeners.LabelInformation;

import com.sun.j3d.utils.scenegraph.io.UnsupportedUniverseException;

/**
 * Class that will handle the interactions between modules (view, model, mapping etc.)
 * 
 * @author David
 * 
 */

// TODO find a way to avoid serialize javaelements
// TODO validate for existing file of visualization and ovewrite if necesary
// TODO ask for location to load visualization file
// TODO icons to save/load visualization
// TODO open editor on double click
// TODO save/open visualization state in workspace
// TODO organize and document code (container and polycyliner transient)
// TODO develop infrastructure to support relationships
// TODO implement relationships base common, lines, arcs, groups,...
// TODO handle max zoom out, limit to bounds (partially solved)
// TODO perform memory optimizations. Add log information in order to avoid unnecesary calls
// TODO containers of different components (container as a method to show relationships) ??
// TODO translation of polycylinders from container to container ??
// TODO extension point so other plugins can extend the visualization ??
public class SeeIT3DManager {

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

	private final Preferences preferences;

	private IColorScale colorScale;

	private String currentProject;

	public SeeIT3DManager() {
		preferences = Preferences.getInstance();
		sceneGraphHandler = new SceneGraphHandler(this, preferences);
		state = new VisualizationState(this);
		colorScale = new ColdToHotColorScale();
	}

	private IMappingView mappingView = null;

	private boolean isSynchronzationWithPackageExplorerSet = false;

	private SelectionInformationAware selectionInformatioAware;

	public synchronized void addContainerToView(Container container) {
		state.addContainerToView(container);
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
			// TODO update one container not all visualization
			refreshVisualization();
		}
		updateMappingView();
	}

	public synchronized List<Container> getCurrentSelectedContainers() {
		Iterator<Container> iteratorOnSelectedContainers = state.iteratorOnSelectedContainers();
		List<Container> selectedContainers = new ArrayList<Container>();
		while (iteratorOnSelectedContainers.hasNext()) {
			selectedContainers.add(iteratorOnSelectedContainers.next());
		}
		return Collections.unmodifiableList(selectedContainers);
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

	/**************************************/
	/******* OPERATIONS ON VIEW PROPERTIES **/
	public synchronized void registerMappingView(IMappingView newMappingView) {
		mappingView = newMappingView;
	}

	public void registerSelectionInformatioAware(LabelInformation selectionInformatioAware) {
		this.selectionInformatioAware = selectionInformatioAware;
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
				transform.setScale(transform.getScale() + preferences.getScaleStep());
			} else {
				transform.setScale(transform.getScale() - preferences.getScaleStep());
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

	private synchronized void setSelectionToView(String viewId, ISelection newSelection) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
		view.getSite().getSelectionProvider().setSelection(newSelection);
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
			// TODO update one container not all visualization
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

	public synchronized VisualProperty getCurrentSortingProperty() {
		return state.getSortingProperty();
	}

	public Iterator<Container> iteratorOnAllContainers() {
		return state.iteratorOnAllContainers();
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

	public synchronized void initializeVisualization(SeeIT3DCanvas canvas) {
		sceneGraphHandler.initializeVisualization(canvas);
	}

	public IColorScale getColorScale() {
		return colorScale;
	}

	public synchronized void setColorScale(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	public synchronized SceneGraphHandler getSceneGraphHandler() {
		return sceneGraphHandler;
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

			if (added % preferences.getContainersPerRow() == 0) {
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

	public void setCurrentProject(String currentProject) {
		this.currentProject = currentProject;
	}

	public synchronized String getCurrentProject() {
		return currentProject;
	}

	@SuppressWarnings("unchecked")
	public synchronized void loadUniverse(IFile file) throws IOException, CoreException {
		ObjectInputStream in = new ObjectInputStream(file.getContents());
		try {
			List<Container> containers = (List<Container>) in.readObject();
			for (Container container : containers) {
				state.addContainerToView(container);
			}
			refreshVisualization();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		sceneGraphHandler.loadUniverse(file);
	}

	public void saveVisualization(IFile file) throws IOException, UnsupportedUniverseException, CoreException {
		// sceneGraphHandler.saveVisualization(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(output);
		List<Container> allContainers = new ArrayList<Container>();
		Iterator<Container> iterator = iteratorOnAllContainers();
		while(iterator.hasNext()){
			Container container = iterator.next();
			allContainers.add(container);
		}
		out.writeObject(allContainers);
		out.close();

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		file.create(input, true, null);

	}

}