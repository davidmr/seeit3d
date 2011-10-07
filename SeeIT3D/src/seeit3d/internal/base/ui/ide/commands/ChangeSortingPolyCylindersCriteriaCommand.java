/**
 * 
 *  Copyright 2011 David Montaño
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package seeit3d.internal.base.ui.ide.commands;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;

import seeit3d.internal.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.model.VisualProperty;
import seeit3d.internal.base.ui.actions.ApplyChangeSortingProperty;

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

	public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
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
