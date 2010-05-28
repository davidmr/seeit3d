package seeit3d.ui.api.imp;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.part.FileEditorInput;

import seeit3d.core.model.IEclipseResourceRepresentation;
import seeit3d.core.model.PolyCylinder;
import seeit3d.general.error.ErrorHandler;
import seeit3d.ui.api.SeeIT3DUI;

public class SeeIT3DUIManager implements SeeIT3DUI {

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
