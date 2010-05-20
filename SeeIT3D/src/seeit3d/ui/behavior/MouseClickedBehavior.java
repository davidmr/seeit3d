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
package seeit3d.ui.behavior;

import javax.media.j3d.*;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.model.Container;
import seeit3d.core.model.PolyCylinder;
import seeit3d.utils.ViewConstants;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

/**
 * This class is called when an object in the scene is selected, and handle the input correctly to open or not the text editor.
 * 
 * @author David Monta�o
 * 
 */
public class MouseClickedBehavior extends PickMouseBehavior {

	private static final int DELTA_PRESS_MBUTTON = 250;

	private final SeeIT3DManager manager;

	private long lastPressedTime = 0;

	public MouseClickedBehavior(Canvas3D canvas, BranchGroup rootBg, Bounds bounds) {
		super(canvas, rootBg, bounds);
		setSchedulingBounds(bounds);
		pickCanvas.setTolerance(ViewConstants.PICKING_TOLERANCE);
		pickCanvas.setMode(PickTool.GEOMETRY);
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void updateScene(int xPos, int yPos) {

		PickResult pickResult = null;

		Container selectedContainer = null;
		PolyCylinder selectedPolyCylinder = null;

		pickCanvas.setShapeLocation(xPos, yPos);
		pickResult = pickCanvas.pickClosest();
		if (pickResult != null) {
			selectedContainer = findContainerAssociated(pickResult);
			selectedPolyCylinder = findPolyCylinderAssociated(pickResult);
		}
		if (doubleClick()) {
			if (selectedPolyCylinder != null) {
				manager.openEditor(selectedPolyCylinder);
			}
		} else {
			boolean toggleContainerSelection = mevent.isControlDown();
			boolean togglePolycylinderSelection = mevent.isShiftDown();
			manager.changeSelectionAndUpdateMappingView(selectedContainer, selectedPolyCylinder, toggleContainerSelection, togglePolycylinderSelection);
		}

		lastPressedTime = System.currentTimeMillis();
	}

	private Container findContainerAssociated(PickResult pickResult) {
		TransformGroup containerTransformGroup = (TransformGroup) pickResult.getNode(PickResult.TRANSFORM_GROUP);
		if (containerTransformGroup != null) {
			Node branchGroupParent = containerTransformGroup.getParent();
			return (Container) branchGroupParent.getUserData();
		}
		return null;
	}

	private PolyCylinder findPolyCylinderAssociated(PickResult pickResult) {
		SceneGraphPath sceneGraphPath = pickResult.getSceneGraphPath();
		for (int i = 0; i < sceneGraphPath.nodeCount(); i++) {
			Node node = sceneGraphPath.getNode(i);
			if (node instanceof Box) {
				return (PolyCylinder) node.getUserData();
			}
		}
		return null;
	}

	private boolean doubleClick() {
		long currentTime = System.currentTimeMillis();
		return currentTime - lastPressedTime < DELTA_PRESS_MBUTTON;
	}

}