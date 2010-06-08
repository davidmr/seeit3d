package seeit3d.ui.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.general.bus.*;
import seeit3d.general.bus.events.*;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.IEclipseResourceRepresentation;
import seeit3d.general.model.PolyCylinder;
import seeit3d.ui.ide.view.SeeIT3DView;
import seeit3d.utils.Utils;

public class SeeIT3DUIHandler implements IEventListener {

	public SeeIT3DUIHandler() {
		EventBus.registerListener(SynchronizePackageExplorerVsViewEvent.class, this);
		EventBus.registerListener(OpenEditorEvent.class, this);
		EventBus.registerListener(OpenSeeIT3DViewEvent.class, this);
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
