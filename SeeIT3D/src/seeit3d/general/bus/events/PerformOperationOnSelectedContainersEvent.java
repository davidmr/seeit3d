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
package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.bus.utils.FunctionToApplyOnContainer;

/**
 * Event triggered when any kind of operation needs to be applied to the selected containers set
 * 
 * @author David Montaño
 * 
 */
public class PerformOperationOnSelectedContainersEvent implements IEvent {

	private final FunctionToApplyOnContainer function;

	private final boolean visualizationNeedsRefresh;

	public PerformOperationOnSelectedContainersEvent(FunctionToApplyOnContainer function, boolean visualizationNeedsRefresh) {
		this.function = function;
		this.visualizationNeedsRefresh = visualizationNeedsRefresh;
	}

	public FunctionToApplyOnContainer getFunction() {
		return function;
	}

	public boolean isVisualizationNeedsRefresh() {
		return visualizationNeedsRefresh;
	}

}
