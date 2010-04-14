package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

/**
 * Command handler to delete the current selected container
 * @author David
 *
 */
public class DeleteContainerCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public DeleteContainerCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.deleteSelectedContainers();
		return null;
	}

}
