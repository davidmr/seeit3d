package seeit3d.ui.api.imp;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.core.model.IEclipseResourceRepresentation;
import seeit3d.core.model.PolyCylinder;
import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.IEventListener;
import seeit3d.general.bus.events.SynchronizePackageExplorerVsView;
import seeit3d.general.error.ErrorHandler;
import seeit3d.ui.api.SeeIT3DUI;
import seeit3d.utils.Utils;

public class SeeIT3DUIManager implements SeeIT3DUI, IEventListener {

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof SynchronizePackageExplorerVsView) {
			Iterator<PolyCylinder> polycylinders = ((SynchronizePackageExplorerVsView) event).getIteratorOnSelectedPolycylinders();
			activateSelection(polycylinders);
		}
	}

	private synchronized void activateSelection(final Iterator<PolyCylinder> polycylinders) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				boolean hasJava = false;
				List<Object> objectsInSelection = new ArrayList<Object>();
				while (polycylinders.hasNext()) {
					PolyCylinder poly = polycylinders.next();
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

	private synchronized void setSelectionToView(String viewId, ISelection newSelection) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(viewId);
		view.getSite().getSelectionProvider().setSelection(newSelection);
	}

	@Override
	public synchronized void openEditor(final PolyCylinder selectedPolyCylinder) {
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

}
