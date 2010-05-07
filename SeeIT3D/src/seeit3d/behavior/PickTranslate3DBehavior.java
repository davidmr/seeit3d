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
package seeit3d.behavior;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.pickfast.PickTool;
import com.sun.j3d.utils.pickfast.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickingCallback;
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
		translate = new MouseTranslate3D(MouseBehavior.MANUAL_WAKEUP, viewingPlatform);
		translate.setTransformGroup(currGrp);
		currGrp.addChild(translate);
		translate.setSchedulingBounds(bounds);
		this.setSchedulingBounds(bounds);
		translate.setupCallback(this);
		callbacks = new ArrayList<PickingCallback>();
	}

	@Override
	public void updateScene(int xpos, int ypos) {
		pickCanvas.setShapeLocation(xpos, ypos);

		pickCanvas.setFlags(PickInfo.NODE | PickInfo.SCENEGRAPHPATH);
		PickInfo pickInfo = pickCanvas.pickClosest();

		if (pickInfo != null) {
			currentTG = (TransformGroup) pickCanvas.getNode(pickInfo, PickTool.TYPE_TRANSFORM_GROUP);
			if ((currentTG != null) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (currentTG.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
				translate.setTransformGroup(currentTG);
				translate.wakeup();
			}
		}
	}

	public void setupCallback(PickingCallback callback) {
		if (callback != null && !this.callbacks.contains(callback)) {
			this.callbacks.add(callback);
		}
	}

	@Override
	public void transformChanged(int type, Transform3D transform) {
		for (PickingCallback callback : this.callbacks) {
			callback.transformChanged(PickingCallback.TRANSLATE, currentTG);
		}
	}

}
