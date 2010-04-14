package seeit3d.commands;

import java.io.IOException;

import javax.media.j3d.DanglingReferenceException;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import seeit3d.error.ErrorHandler;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.utils.ViewConstants;

import com.sun.j3d.utils.scenegraph.io.UnsupportedUniverseException;

public class SaveCurrentVisualizationCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public SaveCurrentVisualizationCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {

			String currentProject = manager.getCurrentProject();
			if (currentProject == null) {
				ErrorHandler.error("None project to store file was selected");
				return null;
			}
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(currentProject);
			IPath path = project.getFullPath();
			IPath newPath = path.append(ViewConstants.VISUALIZATION_FILENAME).addFileExtension(ViewConstants.VISUALIZATION_EXTENSION);
			IFile file = root.getFile(newPath);

			manager.saveVisualization(file);
		
		} catch (CoreException e) {
			ErrorHandler.error("Error while saving visualization");
			e.printStackTrace();
		} catch (IOException e) {
			ErrorHandler.error("Error while saving visualization");
			e.printStackTrace();
		} catch (DanglingReferenceException e) {
			ErrorHandler.error("Error while saving visualization");
			e.printStackTrace();
		} catch (UnsupportedUniverseException e) {
			ErrorHandler.error("Error while saving visualization");
			e.printStackTrace();
		}

		return null;
	}

}
