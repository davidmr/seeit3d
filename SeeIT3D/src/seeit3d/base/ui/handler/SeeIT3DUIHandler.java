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
package seeit3d.base.ui.handler;

import static seeit3d.base.bus.EventBus.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.base.bus.IEvent;
import seeit3d.base.bus.IEventListener;
import seeit3d.base.bus.events.*;
import seeit3d.base.error.ErrorHandler;
import seeit3d.base.model.*;
import seeit3d.base.ui.behavior.PickUtils;
import seeit3d.base.ui.ide.view.SeeIT3DView;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;

/**
 * Class to handle interactions with the user interface and IDE
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DUIHandler implements IEventListener {

	public SeeIT3DUIHandler() {
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
				boolean hasJava = false;
				List<Object> objectsInSelection = new ArrayList<Object>();
				for (PolyCylinder poly : polycylinders) {
					IEclipseResourceRepresentation representation = poly.getRepresentation();
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

	private void setSelectionToView(String viewId, ISelection newSelection) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
		view.getSite().getSelectionProvider().setSelection(newSelection);
	}

	private void openEditor(final PolyCylinder selectedPolyCylinder) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IEclipseResourceRepresentation resource = selectedPolyCylinder.getRepresentation();
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
