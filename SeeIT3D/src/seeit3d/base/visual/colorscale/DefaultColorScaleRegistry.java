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
package seeit3d.base.visual.colorscale;

import java.util.*;

import seeit3d.base.visual.api.IColorScaleRegistry;

import com.google.inject.Singleton;

/**
 * Default implementation of the color scales registry
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class DefaultColorScaleRegistry implements IColorScaleRegistry {

	private final List<IColorScale> colorScales;

	public DefaultColorScaleRegistry() {
		colorScales = new ArrayList<IColorScale>();
	}

	@Override
	public void registerColorScale(IColorScale... colorScale) {
		colorScales.addAll(Arrays.asList(colorScale));
	}


	public Iterable<IColorScale> allColorScales() {
		return colorScales;
	}

	public IColorScale findByName(String colorScaleName) {
		for (IColorScale colorScale : colorScales) {
			if (colorScale.getName().equals(colorScaleName)) {
				return colorScale;
			}
		}
		return null;
	}
}
