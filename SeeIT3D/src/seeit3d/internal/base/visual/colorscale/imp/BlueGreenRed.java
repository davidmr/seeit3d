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
package seeit3d.internal.base.visual.colorscale.imp;

import javax.vecmath.Color3f;

import seeit3d.internal.base.visual.colorscale.IColorScale;

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
	public Color3f generate(float value) {
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
