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
package seeit3d.visual.colorscale.imp;

import javax.vecmath.Color3f;

import seeit3d.visual.colorscale.IColorScale;

/**
 * Implementation of the Blue to Red passing through green color scale
 * 
 * @author David Montaño
 * 
 */
public class BlueGreenRed implements IColorScale {

	private static final float MID_POINT = 0.5f;

	private static final float MID_POINT_INVERSE = 1 / MID_POINT;

	public BlueGreenRed() {}

	@Override
	public Color3f generateCuantitavieColor(float value) {
		float r = (MID_POINT_INVERSE * (value - MID_POINT));
		float g = value < MID_POINT ? (MID_POINT_INVERSE * value) : (1.0f - MID_POINT_INVERSE * (value - MID_POINT));
		float b = 1.0f - MID_POINT_INVERSE * value;

		r = inRange(r);
		g = inRange(g);
		b = inRange(b);

		return new Color3f(r, g, b);
	}

	private float inRange(float value) {
		float inRange = value;
		inRange = Math.min(inRange, 1.0f);
		inRange = Math.max(inRange, 0.0f);
		return inRange;
	}

	@Override
	public String getName() {
		return "Blue Green Red";
	}

}
