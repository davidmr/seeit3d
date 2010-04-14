package seeit3d.utils;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import seeit3d.error.ErrorHandler;
import seeit3d.view.SeeIT3DView;

public class OpenSeeIT3DView implements Runnable {

	public OpenSeeIT3DView() {
	}

	@Override
	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SeeIT3DView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
			ErrorHandler.error("An error ocurred while trying to open the SeeIT3D view");
		}

	}

}