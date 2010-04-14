package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

public class ScaleDownContainerCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public ScaleDownContainerCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.scaleCurrentContainer(false);
		return null;
	}

}
