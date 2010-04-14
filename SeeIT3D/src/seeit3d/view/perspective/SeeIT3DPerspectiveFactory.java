package seeit3d.view.perspective;

import org.eclipse.ui.*;

import seeit3d.utils.Utils;
import seeit3d.view.*;

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
