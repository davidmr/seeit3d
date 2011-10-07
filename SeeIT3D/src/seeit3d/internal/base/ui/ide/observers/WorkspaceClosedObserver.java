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
package seeit3d.internal.base.ui.ide.observers;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import seeit3d.internal.base.bus.events.DeleteContainersEvent;

/**
 * This class listens to the close event of projects within the workspace and cleans the visualization according to that.
 * 
 * @author David Montaño
 * 
 */
public class WorkspaceClosedObserver implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource resource = event.getResource();
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE && resource != null && resource.getType() == IResource.PROJECT) {
			publishEvent(new DeleteContainersEvent(true));
		}

	}
}
