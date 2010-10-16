package seeit3d.base.ui.ide.commands;

import static seeit3d.base.bus.EventBus.*;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import seeit3d.base.bus.events.VisualizePolycylinderAsContainerEvent;

public class VisualizePolycylinderAsContainerCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		publishEvent(new VisualizePolycylinderAsContainerEvent());
		return null;
	}

}
