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

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.picking.behaviors.PickingCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class wraps the MouseTranslate3D behavior to allow the user to pick objects and translate them in the scene. It is based on the PickTranslateBehavior from Java 3D
 * 
 * @author David Montaño
 * 
 */
public class PickTranslate3DBehavior extends PickMouseBehavior implements MouseBehaviorCallback {

	private final MouseTranslate3D translate;

	private final List<PickingCallback> callbacks;

	private TransformGroup currentTG;

	public PickTranslate3DBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds, ViewingPlatform viewingPlatform) {
		super(canvas, root, bounds);
		setSchedulingBounds(bounds);
		pickCanvas.setMode(PickTool.GEOMETRY);
		translate = new MouseTranslate3D(MouseBehavior.MANUAL_WAKEUP, viewingPlatform);
		translate.setTransformGroup(currGrp);
		currGrp.addChild(translate);
		translate.setSchedulingBounds(bounds);
		translate.setupCallback(this);
		callbacks = new ArrayList<PickingCallback>();
	}

	@Override
	public void updateScene(int xpos, int ypos) {
		pickCanvas.setShapeLocation(xpos, ypos);

		PickResult[] pickResults = pickCanvas.pickAll();
		TransformGroup mainTransformGroup = PickUtils.chooseContainerMainTransformGroup(pickResults);
		if (mainTransformGroup != null) {
			currentTG = mainTransformGroup;
			translate.setTransformGroup(currentTG);
			translate.wakeup();
		}

		// PickResult pickResult = pickCanvas.pickAny();
		//
		// if (pickResult != null) {
		// pickResult.getNode(PickResult.)
		// currentTG = (TransformGroup) pickResult.getNode(PickResult.TRANSFORM_GROUP);
		// if ((currentTG != null) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
		// translate.setTransformGroup(currentTG);
		// translate.wakeup();
		// }
		// }
	}

	public void setupCallback(PickingCallback callback) {
		if (callback != null && !this.callbacks.contains(callback)) {
			this.callbacks.add(callback);
		}
	}

	public void unregisterCallback(PickingCallback callback) {
		if (callback != null) {
			this.callbacks.remove(callback);
		}
	}

	@Override
	public void transformChanged(int type, Transform3D transform) {
		for (PickingCallback callback : this.callbacks) {
			callback.transformChanged(PickingCallback.TRANSLATE, currentTG);
		}
	}

}
