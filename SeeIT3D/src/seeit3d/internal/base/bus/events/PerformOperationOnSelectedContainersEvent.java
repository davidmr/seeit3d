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
package seeit3d.internal.base.bus.events;

import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;

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
