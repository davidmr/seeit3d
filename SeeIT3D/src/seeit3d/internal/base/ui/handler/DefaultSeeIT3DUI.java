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
package seeit3d.internal.base.ui.handler;

import static seeit3d.internal.base.bus.EventBus.publishEvent;
import static seeit3d.internal.base.bus.EventBus.registerListener;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.analysis.IEclipseResourceRepresentation;
import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.ChangeSelectionEvent;
import seeit3d.internal.base.bus.events.OpenEditorEvent;
import seeit3d.internal.base.bus.events.OpenSeeIT3DViewEvent;
import seeit3d.internal.base.bus.events.SelectionToolEndedEvent;
import seeit3d.internal.base.bus.events.SynchronizePackageExplorerVsViewEvent;
import seeit3d.internal.base.error.ErrorHandler;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;
import seeit3d.internal.base.ui.api.ISeeIT3DUI;
import seeit3d.internal.base.ui.behavior.PickUtils;
import seeit3d.internal.base.ui.ide.view.SeeIT3DView;
import seeit3d.internal.utils.Utils;
import seeit3d.internal.utils.ViewConstants;

import com.google.inject.Singleton;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;

/**
 * Class to handle interactions with the user interface and IDE
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultSeeIT3DUI implements ISeeIT3DUI, IEventListener {

	public DefaultSeeIT3DUI() {
		registerListener(SynchronizePackageExplorerVsViewEvent.class, this);
		registerListener(OpenEditorEvent.class, this);
		registerListener(OpenSeeIT3DViewEvent.class, this);
		registerListener(SelectionToolEndedEvent.class, this);
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof SynchronizePackageExplorerVsViewEvent) {
			Iterable<PolyCylinder> polycylinders = ((SynchronizePackageExplorerVsViewEvent) event).selectedPolycylinders();
			activateSelection(polycylinders);
		}

		if (event instanceof OpenEditorEvent) {
			openEditor(((OpenEditorEvent) event).getPolyCylinder());
		}

		if (event instanceof OpenSeeIT3DViewEvent) {
			openSeeIT3DView();
		}

		if (event instanceof SelectionToolEndedEvent) {
			SelectionToolEndedEvent selectionToolEndedEvent = (SelectionToolEndedEvent) event;
			Rectangle selection = selectionToolEndedEvent.getSelection();
			PickCanvas pickCanvas = selectionToolEndedEvent.getPickCanvas();
			pickObjects(selection, pickCanvas);
		}

	}

	private void pickObjects(Rectangle selection, PickCanvas pickCanvas) {
		int step = ViewConstants.SELECTION_TOOL_STEP;
		pickCanvas.setTolerance(ViewConstants.SELECTION_TOOL_STEP + 1);
		List<Container> containers = new ArrayList<Container>();
		List<PolyCylinder> polycylinders = new ArrayList<PolyCylinder>();
		for (int i = 0; i < selection.width; i += step) {
			for (int j = 0; j < selection.height; j += step) {

				int x = selection.x + i;
				x = Math.min(x, selection.x + i - step);
				x = Math.max(x, selection.x + step);

				int y = selection.y + j;
				y = Math.min(y, selection.y + j - step);
				y = Math.max(y, selection.y + step);

				pickCanvas.setShapeLocation(x, y);

				PickResult[] pickResult = pickCanvas.pickAllSorted();
				Container selectedContainer = PickUtils.findContainerAssociated(pickResult);
				List<PolyCylinder> selectedPolycylinder = PickUtils.findPolyCylinderAssociated(pickResult);
				if (selectedContainer != null && !containers.contains(selectedContainer)) {
					containers.add(selectedContainer);
				}
				for (PolyCylinder polyCylinder : selectedPolycylinder) {
					if (!polycylinders.contains(polyCylinder)) {
						polycylinders.add(polyCylinder);
					}
				}
			}
		}
		publishEvent(new ChangeSelectionEvent(containers, polycylinders, true, true));
	}

	private void activateSelection(final Iterable<PolyCylinder> polycylinders) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				List<Object> objectsInSelection = new ArrayList<Object>();
				for (PolyCylinder poly : polycylinders) {
					Object eclipseSeletable = poly.getRepresentation().getEclipseSeletable();
					if (eclipseSeletable != null) {
						objectsInSelection.add(eclipseSeletable);
					}
				}
				ISelection newSelection = new StructuredSelection(objectsInSelection);
				setSelectionToView(Utils.NAVIGATOR_VIEW_ID, newSelection);
				setSelectionToView(Utils.ID_PACKAGE_EXPLORER, newSelection);
			}
		});
	}

	private void setSelectionToView(String viewId, ISelection newSelection) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
		view.getSite().getSelectionProvider().setSelection(newSelection);
	}

	private void openEditor(final PolyCylinder selectedPolyCylinder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {

				IEclipseResourceRepresentation representation = selectedPolyCylinder.getRepresentation();
				IResource associatedResource = representation.getResource();
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

	private void openSeeIT3DView() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SeeIT3DView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				} catch (PartInitException e) {
					ErrorHandler.error("An error ocurred while trying to open the SeeIT3D view");
				}

			}
		});

	}

}
