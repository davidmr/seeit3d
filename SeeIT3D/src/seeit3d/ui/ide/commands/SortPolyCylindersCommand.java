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
package seeit3d.ui.ide.commands;

import static seeit3d.general.bus.EventBus.publishEvent;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import seeit3d.general.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.general.bus.utils.FunctionToApplyOnContainer;
import seeit3d.general.model.Container;

/**
 * Command to sort the polycylinders within the selected containers
 * 
 * @author David Montaño
 * 
 */
public class SortPolyCylindersCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		publishEvent(createEvent());
		return null;
	}

	public PerformOperationOnSelectedContainersEvent createEvent() {
		FunctionToApplyOnContainer function = new FunctionToApplyOnContainer() {
			@Override
			public Container apply(Container container) {
				container.setSorted(true);
				container.updateVisualRepresentation();
				return container;
			}
		};
		return new PerformOperationOnSelectedContainersEvent(function, true);
	}

}
