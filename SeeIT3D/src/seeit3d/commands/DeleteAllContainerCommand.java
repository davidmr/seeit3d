package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

/**
 * Command handler for deleting all containers in view
 * @author David
 *
 */
public class DeleteAllContainerCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public DeleteAllContainerCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.deleteAllContainers();
		return null;
	}

}
