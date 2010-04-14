package seeit3d.behavior;

import java.awt.event.MouseEvent;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;

import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * Based on MouseTranslate
 * 
 * @author david
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
