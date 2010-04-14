package seeit3d.commands;

import java.util.Map;

import org.eclipse.core.commands.*;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.*;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.model.representation.VisualProperty;

public class ChangeSortingPolyCylindersCriteriaCommand extends AbstractHandler implements IElementUpdater {

	private static final String SORT_BY_PARAMETER = "seeit3d.commands.sortByParameter";

	public static final String CHANGE_SORT_COMMAND_ID = "seeit3d.orderPolyCylindersByCriteria";

	private final SeeIT3DManager manager;

	private String currentCriteria;

	public ChangeSortingPolyCylindersCriteriaCommand() {
		manager = SeeIT3DManager.getInstance();
		currentCriteria = manager.getCurrentSortingProperty().toString();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String param = event.getParameter(SORT_BY_PARAMETER);
		if (param.equals(currentCriteria)) {
			return null;
		}

		currentCriteria = param;

		VisualProperty property = VisualProperty.valueOf(currentCriteria);
		manager.changeCurrentSortingPolyCylindersProperty(property);
		ICommandService service = (ICommandService) HandlerUtil.getActiveWorkbenchWindowChecked(event).getService(ICommandService.class);
		service.refreshElements(event.getCommand().getId(), null);

		return null;
	}

	@SuppressWarnings("unchecked")
	public void updateElement(UIElement element, Map parameters) {
		String parm = (String) parameters.get(SORT_BY_PARAMETER);
		if (parm != null) {
			if (currentCriteria != null && currentCriteria.equals(parm)) {
				element.setChecked(true);
			} else {
				element.setChecked(false);
			}
		}
	}
}
