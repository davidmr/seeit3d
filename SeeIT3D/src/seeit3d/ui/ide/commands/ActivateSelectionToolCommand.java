package seeit3d.ui.ide.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import seeit3d.general.bus.events.ActivateSelectionToolEvent;

import static seeit3d.general.bus.EventBus.*;

public class ActivateSelectionToolCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		publishEvent(new ActivateSelectionToolEvent());
		return null;
	}

}
