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
package seeit3d.ui.ide.perspective;

import org.eclipse.ui.*;

import seeit3d.ui.ide.view.SeeIT3DView;
import seeit3d.utils.Utils;

/**
 * Creates the layout of the seeit3d perspective
 * 
 * @author David
 * 
 */
public class SeeIT3DPerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, 0.2f, layout.getEditorArea());
		folder.addView(Utils.ID_PACKAGE_EXPLORER);
		folder.addView(Utils.NAVIGATOR_VIEW_ID);
		layout.addView(SeeIT3DView.ID, IPageLayout.TOP, 1.0f, layout.getEditorArea());
		layout.setEditorAreaVisible(false);

	}

}
