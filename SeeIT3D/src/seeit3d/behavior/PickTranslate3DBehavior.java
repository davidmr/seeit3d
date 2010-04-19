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

import javax.media.j3d.*;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.pickfast.PickTool;
import com.sun.j3d.utils.pickfast.behaviors.PickMouseBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class wraps the MouseTranslate3D behavior to allow the user to pick objects and translate them in the scene. It is based on the PickTranslateBehavior from Java 3D
 * 
 * @author David Montaño
 * 
 */
public class PickTranslate3DBehavior extends PickMouseBehavior {

	private final MouseTranslate3D translate;

	public PickTranslate3DBehavior(Canvas3D canvas, BranchGroup root, Bounds bounds, ViewingPlatform viewingPlatform) {
		super(canvas, root, bounds);
		translate = new MouseTranslate3D(MouseBehavior.MANUAL_WAKEUP, viewingPlatform);
		translate.setTransformGroup(currGrp);
		currGrp.addChild(translate);
		translate.setSchedulingBounds(bounds);
		this.setSchedulingBounds(bounds);
	}

	@Override
	public void updateScene(int xpos, int ypos) {
		pickCanvas.setShapeLocation(xpos, ypos);

		pickCanvas.setFlags(PickInfo.NODE | PickInfo.SCENEGRAPHPATH);
		PickInfo pickInfo = pickCanvas.pickClosest();

		if (pickInfo != null) {
			TransformGroup tg = (TransformGroup) pickCanvas.getNode(pickInfo, PickTool.TYPE_TRANSFORM_GROUP);
			if ((tg != null) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
				translate.setTransformGroup(tg);
				translate.wakeup();
			}
		}
	}

}
