package seeit3d.base.ui.ide.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import seeit3d.base.bus.events.ActivateSelectionToolEvent;

import static seeit3d.base.bus.EventBus.*;

public class ActivateSelectionToolCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		publishEvent(new ActivateSelectionToolEvent());
		return null;
	}

}
