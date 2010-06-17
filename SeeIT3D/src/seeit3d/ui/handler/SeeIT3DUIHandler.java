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
package seeit3d.ui.handler;

import static seeit3d.general.bus.EventBus.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.*;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.IEclipseResourceRepresentation;
import seeit3d.general.model.PolyCylinder;
import seeit3d.ui.ide.view.SeeIT3DView;
import seeit3d.utils.Utils;

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
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof SynchronizePackageExplorerVsViewEvent) {
			List<PolyCylinder> polycylinders = ((SynchronizePackageExplorerVsViewEvent) event).getIteratorOnSelectedPolycylinders();
			activateSelection(polycylinders);
		}

		if (event instanceof OpenEditorEvent) {
			openEditor(((OpenEditorEvent) event).getPolyCylinder());
		}

		if (event instanceof OpenSeeIT3DViewEvent) {
			openSeeIT3DView();
		}
	}

	private synchronized void activateSelection(final List<PolyCylinder> polycylinders) {
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
		Display.getDefault().syncExec(new Runnable() {

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
