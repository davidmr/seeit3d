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
package seeit3d.base.ui.ide.view.listeners;

import javax.vecmath.Color3f;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;

import seeit3d.base.visual.colorscale.IColorScale;

/**
 * This <code>PaintListener</code> is in charge of drawing a preview of a color scale as a bar that the user can easily understand
 * 
 * @author David Montaño
 * 
 */
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
