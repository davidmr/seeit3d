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

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.event.MouseEvent;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class handles the rotation of elements in the scene, taking into account the position of the user in the world
 * 
 * @author David Montaño
 * 
 */
public class MouseRotate3D extends MouseOperationBehavior {

	private static final float factor = 0.02f;

	public MouseRotate3D(int format, ViewingPlatform viewingPlatform) {
		super(format, viewingPlatform);
	}

	@Override
	public int getMouseButtonToWakeUp() {
		return MouseEvent.BUTTON1;
	}

	@Override
	protected int operationToNotifyOnCallbackType() {
		return MouseBehaviorCallback.ROTATE;
	}

	@Override
	public Transform3D buildTransformation(Transform3D transform, float dx, float dy) {
		float dyCorrected = dy * factor;
		float dxCorrected = dx * factor;

		Matrix4d xRotation = new Matrix4d(1.0, 0.0, 0.0, 0.0, 0.0, cos(dyCorrected), -sin(dyCorrected), 0.0, 0.0, sin(dyCorrected), cos(dyCorrected), 0.0, 0.0, 0.0, 0.0, 1.0);

		Matrix4d yRotation = new Matrix4d(cos(dxCorrected), 0.0, sin(dxCorrected), 0.0, 0.0, 1.0, 0.0, 0.0, -sin(dxCorrected), 0.0, cos(dxCorrected), 0.0, 0.0, 0.0, 0.0, 1.0);

		Matrix3d viewRotMatrix = getRotationFromViewersPosition();
		Matrix4d result = new Matrix4d();
		result.set(viewRotMatrix);

		result.mul(xRotation);
		result.mul(yRotation);

		viewRotMatrix.invert(viewRotMatrix);
		Matrix4d invert = new Matrix4d();
		invert.set(viewRotMatrix);
		result.mul(invert);

		Matrix3f rotation = new Matrix3f();
		transform.get(rotation);

		Matrix4d temporal = new Matrix4d();
		temporal.set(rotation);

		result.mul(temporal);

		// store old pos
		Vector3d position = new Vector3d();
		transform.get(position);

		transform.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		transform.setRotation(new Matrix3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f));

		Transform3D modified = new Transform3D(result);
		transform.mul(transform, modified);

		// restore old pos
		transform.setTranslation(position);

		return transform;
	}

}
