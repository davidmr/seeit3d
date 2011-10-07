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
package seeit3d.internal.base.ui.ide.view.listeners;

import javax.vecmath.Color3f;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;

import seeit3d.internal.base.visual.colorscale.IColorScale;

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
			Color3f colorFromScale = colorScale.generate((float) i / (float) width);
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
