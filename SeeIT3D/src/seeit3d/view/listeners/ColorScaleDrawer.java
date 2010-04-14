package seeit3d.view.listeners;

import javax.vecmath.Color3f;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import seeit3d.colorscale.IColorScale;

public class ColorScaleDrawer implements PaintListener {

	private final IColorScale colorScale;

	public ColorScaleDrawer(IColorScale colorScale) {
		this.colorScale = colorScale;
	}

	@Override
	public void paintControl(PaintEvent event) {
		GC gc = event.gc;
		Canvas canvas = (Canvas) event.widget;
		Point size = canvas.getSize();
		int width = size.x;
		int height = size.y;
		for (int i = 0; i < width; i++) {
			Color3f colorFromScale = colorScale.generateCuantitavieColor((float) i / (float) width);
			Color swtColor = createColorSWTFromColor3f(event.display, colorFromScale);
			gc.setBackground(swtColor);
			gc.fillRectangle(i, 0, i + 1, height);
		}
	}

	private Color createColorSWTFromColor3f(Device device, Color3f color3f) {
		int r = (int) Math.floor(color3f.x * 255);
		int g = (int) Math.floor(color3f.y * 255);
		int b = (int) Math.floor(color3f.z * 255);
		return new Color(device, r, g, b);
	}

}
