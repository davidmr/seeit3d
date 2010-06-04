/**
 * Copyright (C) 2010  David Monta�o
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

import seeit3d.general.bus.EventBus;
import seeit3d.general.bus.events.ResetVisualizationEvent;

/**
 * Command to reset the state of the visualization. This must be executed to allow the application of changed preferences.
 * 
 * @author David Monta�o
 * 
 */
public class ResetVisualizationCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		EventBus.publishEvent(new ResetVisualizationEvent());
		return null;
	}

}
