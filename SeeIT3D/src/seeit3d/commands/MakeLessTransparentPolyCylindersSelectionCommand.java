package seeit3d.commands;

import org.eclipse.core.commands.*;

import seeit3d.manager.SeeIT3DManager;

public class MakeLessTransparentPolyCylindersSelectionCommand extends AbstractHandler {

	private final SeeIT3DManager manager;

	public MakeLessTransparentPolyCylindersSelectionCommand() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		manager.changeTransparencyPolyCylindersSelection(false);
		return null;
	}

}
