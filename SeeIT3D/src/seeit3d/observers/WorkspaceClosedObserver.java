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
package seeit3d.observers;

import org.eclipse.core.resources.*;

import seeit3d.manager.SeeIT3DManager;

/**
 * This class listens to the close event of projects within the workspace and cleans the visualization according to that.
 * 
 * @author David Monta�o
 * 
 */
public class WorkspaceClosedObserver implements IResourceChangeListener {

	private final SeeIT3DManager manager;

	public WorkspaceClosedObserver() {
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource resource = event.getResource();
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE && resource != null && resource.getType() == IResource.PROJECT) {
			manager.deleteAllContainers();
		}

	}
}
