package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

public class ScaleUpContainerCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public ScaleUpContainerCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.scaleCurrentContainer(true);
		return null;
	}

}
