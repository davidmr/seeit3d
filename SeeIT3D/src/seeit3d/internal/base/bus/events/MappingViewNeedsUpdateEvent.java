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

import java.util.List;

import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.model.Container;

/**
 * Event triggered when the mapping view needs to be updated to the current state of the visualization
 * 
 * @author David Montaño
 * 
 */
public class MappingViewNeedsUpdateEvent implements IEvent {

	private final List<Container> containers;

	public MappingViewNeedsUpdateEvent(List<Container> containers) {
		this.containers = containers;
	}

	public List<Container> getContainers() {
		return containers;
	}

}
