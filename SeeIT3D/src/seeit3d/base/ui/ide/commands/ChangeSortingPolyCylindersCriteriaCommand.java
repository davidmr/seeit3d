/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.base.ui.ide.commands;

import static seeit3d.base.bus.EventBus.*;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import seeit3d.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.VisualProperty;
import seeit3d.base.ui.actions.ApplyChangeSortingProperty;

/**
 * Command to change the criteria to sort polycylinders when the sorting is executed
 * 
 * @author David Montaño
 * 
 */
public class ChangeSortingPolyCylindersCriteriaCommand extends AbstractHandler implements IElementUpdater {

	private static final String SORT_BY_PARAMETER = "seeit3d.commands.sortByParameter";

	public static final String CHANGE_SORT_COMMAND_ID = "seeit3d.orderPolyCylindersByCriteria";

	private String currentCriteria;

	public ChangeSortingPolyCylindersCriteriaCommand() {
		currentCriteria = VisualProperty.HEIGHT.toString();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String param = event.getParameter(SORT_BY_PARAMETER);
		if (param.equals(currentCriteria)) {
			return null;
		}

		currentCriteria = param;

		VisualProperty property = VisualProperty.valueOf(currentCriteria);

		PerformOperationOnSelectedContainersEvent eventChangeSorting = createEvent(property);
		publishEvent(eventChangeSorting);

		ICommandService service = (ICommandService) HandlerUtil.getActiveWorkbenchWindowChecked(event).getService(ICommandService.class);
		service.refreshElements(event.getCommand().getId(), null);

		return null;
	}

	private PerformOperationOnSelectedContainersEvent createEvent(final VisualProperty property) {
		FunctionToApplyOnContainer function = new ApplyChangeSortingProperty(property);
		return new PerformOperationOnSelectedContainersEvent(function, false);

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
