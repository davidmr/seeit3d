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

import java.awt.event.MouseEvent;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;

import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * This class handles the translation of elements in the scene, taking into account the position of the user in the world
 * 
 * @author David Montaño
 * 
 */
public class MouseTranslate3D extends MouseOperationBehavior {

	private static final float factor = 0.02f;

	public MouseTranslate3D(int format, ViewingPlatform viewingPlatform) {
		super(format, viewingPlatform);
	}

	@Override
	public int getMouseButtonToWakeUp() {
		return MouseEvent.BUTTON3;
	}

	@Override
	public Transform3D buildTransformation(Transform3D transform, float dx, float dy) {

		float dyCorrected = -dy * factor;
		float dxCorrected = dx * factor;

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
