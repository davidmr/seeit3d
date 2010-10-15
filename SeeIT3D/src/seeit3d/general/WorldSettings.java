package seeit3d.general;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

import seeit3d.core.handler.SeeIT3DCanvas;

public class WorldSettings {

	public static Bounds bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 2000.0f);

	private static SeeIT3DCanvas canvas;

	public static SeeIT3DCanvas canvas() {
		if (canvas == null) {
			throw new IllegalStateException("Canvas not defined");
		}
		return canvas;
	}

}
