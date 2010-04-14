package seeit3d.commands;
import java.io.IOException;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import seeit3d.error.ErrorHandler;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.utils.ViewConstants;

public class LoadSavedVisualization extends AbstractHandler {

	private final SeeIT3DManager manager;

	public LoadSavedVisualization() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String currentProject = manager.getCurrentProject();
		if (currentProject == null) {
			ErrorHandler.error("None project to load file was selected");
			return null;
		}

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(currentProject);
		IPath path = project.getFullPath();
		IPath newPath = path.append(ViewConstants.VISUALIZATION_FILENAME).addFileExtension(ViewConstants.VISUALIZATION_EXTENSION);
		IFile file = root.getFile(newPath);

		if (file.exists()) {
			try {
				manager.loadUniverse(file);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		} else {
			ErrorHandler.error("Visualization file not found within project " + currentProject);
		}

		return null;

	}

}
