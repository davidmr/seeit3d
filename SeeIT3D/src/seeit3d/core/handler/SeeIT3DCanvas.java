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
package seeit3d.core.handler;

import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

/**
 * Canvas based on Canvas3D from Java3D. It allows to modify certain parts of the visualization algorithm like the rendering hints
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DCanvas extends Canvas3D {

	private static final long serialVersionUID = 1L;

	private J3DGraphics2D render2D;

	public SeeIT3DCanvas(GraphicsConfiguration configuration) {
		super(configuration);
		render2D = getGraphics2D();
		render2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	}

}
