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

import java.awt.event.MouseEvent;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class handles the translation of elements in the scene, taking into account the position of the user in the world
 * 
 * @author David Montaño
 * 
 */
public class MouseTranslate3D extends MouseOperationBehavior {

	public MouseTranslate3D(int format, ViewingPlatform viewingPlatform) {
		super(format, viewingPlatform);
	}

	@Override
	public int getMouseButtonToWakeUp() {
		return MouseEvent.BUTTON3;
	}

	@Override
	protected int operationToNotifyOnCallbackType() {
		return MouseBehaviorCallback.TRANSLATE;
	}

	@Override
	public Transform3D buildTransformation(Transform3D transform, float dx, float dy) {

		Vector3f viewersPosition = getViewersPosition();

		float distanceToCenter = viewersPosition.length();

		float movementFactor = distanceToCenter / 1000;

		float dyCorrected = -dy * movementFactor;
		float dxCorrected = dx * movementFactor;

		Matrix4d newPosition = new Matrix4d(
				1, 0, 0, dxCorrected,
				0, 1, 0, dyCorrected,
				0, 0, 1, 0,
				0, 0, 0, 1);

		Matrix3d viewRotMatrix = getRotationFromViewersPosition();

		Matrix4d result = new Matrix4d();
		result.set(viewRotMatrix);

		result.mul(newPosition);

		viewRotMatrix.invert(viewRotMatrix);
		Matrix4d invert = new Matrix4d();
		invert.set(viewRotMatrix);

		result.mul(invert);

		Transform3D modified = new Transform3D(result);
		transform.mul(modified, transform);

		return transform;
	}

}
