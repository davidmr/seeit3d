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

import org.eclipse.core.commands.*;

import static seeit3d.general.bus.EventBus.*;
import seeit3d.general.bus.events.KeyBasedChangeSelectionEvent;

/**
 * Command to change the selected container (previous) using the Shift-TAB key
 * 
 * @author David Montaño
 * 
 */
public class ChangeContainerSelectionIncrease extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		publishEvent(new KeyBasedChangeSelectionEvent(true));
		return null;
	}

}
