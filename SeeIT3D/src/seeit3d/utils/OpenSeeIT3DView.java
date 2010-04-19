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
package seeit3d.utils;

import org.eclipse.ui.*;

import seeit3d.error.ErrorHandler;
import seeit3d.view.SeeIT3DView;

/**
 * Runnable class to open the SeeIT3D view
 * 
 * @author David Montaño
 * 
 */
public class OpenSeeIT3DView implements Runnable {

	public OpenSeeIT3DView() {
	}

	@Override
	public void run() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SeeIT3DView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
		} catch (PartInitException e) {
			ErrorHandler.error("An error ocurred while trying to open the SeeIT3D view");
		}

	}

}