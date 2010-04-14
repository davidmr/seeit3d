package seeit3d.view;

import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

public class SeeIT3DCanvas extends Canvas3D {

	private static final long serialVersionUID = 1L;

	private J3DGraphics2D render2D;

	public SeeIT3DCanvas(GraphicsConfiguration configuration) {
		super(configuration);
		render2D = getGraphics2D();
		render2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	}

}
