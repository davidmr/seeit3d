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
package seeit3d.internal.base.ui.ide.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import seeit3d.internal.base.ui.ide.view.SeeIT3DView;
import seeit3d.internal.utils.Utils;

/**
 * Creates the layout of the seeit3d perspective
 * 
 * @author David Montaño
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
