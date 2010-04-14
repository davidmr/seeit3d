package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

public class ChangeContainerSelectionDecrease extends AbstractHandler {

	private final SeeIT3DManager manager;

	public ChangeContainerSelectionDecrease() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.changeContainerSelection(false);
		return null;
	}

}
