package seeit3d.observers;

import org.eclipse.core.resources.*;

import seeit3d.manager.SeeIT3DManager;

public class WorkspaceClosedObserver implements IResourceChangeListener {

	private final SeeIT3DManager manager;

	public WorkspaceClosedObserver() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource resource = event.getResource();
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE && resource != null && resource.getType() == IResource.PROJECT) {
			manager.cleanVisualization();
		}

	}
}
