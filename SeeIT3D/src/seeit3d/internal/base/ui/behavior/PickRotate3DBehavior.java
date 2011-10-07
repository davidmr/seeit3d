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
package seeit3d.internal.base.ui.behavior;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class wraps the MouseRotate3D behavior to allow the user to pick objects and rotate them in the scene. It is based on the PickRotateBehavior from Java 3D
 * 
 * @author David Montaño
 * 
 */
public class PickRotate3DBehavior extends PickMouseBehavior {

	private final MouseRotate3D rotate;

	private TransformGroup currentTG;

	public PickRotate3DBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds, ViewingPlatform viewingPlatform) {

		super(canvas, root, bounds);
		setSchedulingBounds(bounds);
		pickCanvas.setMode(PickTool.GEOMETRY);
		rotate = new MouseRotate3D(MouseBehavior.MANUAL_WAKEUP, viewingPlatform);
		rotate.setTransformGroup(currGrp);
		currGrp.addChild(rotate);
		rotate.setSchedulingBounds(bounds);
	}

	@Override
	public void updateScene(int xpos, int ypos) {

		pickCanvas.setShapeLocation(xpos, ypos);

		PickResult[] pickResults = pickCanvas.pickAll();
		TransformGroup mainTransformGroup = PickUtils.chooseContainerMainTransformGroup(pickResults);
		if (mainTransformGroup != null) {
			currentTG = mainTransformGroup;
			rotate.setTransformGroup(currentTG);
			rotate.wakeup();
		}

		// PickResult pickResult = pickCanvas.pickAny();
		//
		// if (pickResult != null) {
		// currentTG = (TransformGroup) pickResult.getNode(PickResult.TRANSFORM_GROUP);
		// if ((currentTG != null) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
		// rotate.setTransformGroup(currentTG);
		// rotate.wakeup();
		// }
		// }

	}

}
