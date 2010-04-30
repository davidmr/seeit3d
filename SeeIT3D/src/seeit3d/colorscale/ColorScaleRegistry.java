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
package seeit3d.colorscale;

import static com.google.common.collect.Lists.*;

import java.util.Iterator;
import java.util.List;

import seeit3d.colorscale.imp.*;

/**
 * Factory class to provide access to the set of all color scales in the application.
 * 
 * @author David Montaño
 * 
 */
public class ColorScaleRegistry {

	private static final ColorScaleRegistry instance = new ColorScaleRegistry();

	public static ColorScaleRegistry getInstance() {
		return instance;
	}

	private final List<IColorScale> colorScales;

	private ColorScaleRegistry() {
		colorScales = newArrayList(
				new BlueTone(),
				new BlueToYellow(),
				new ColdToHotColorScale(),
				new GrayColorScale(), 
				new HeatedObject(),
				new LinearOptimal(), 
				new MagentaTone(),
				new Rainbow());
	}

	public void registerColorScale(IColorScale colorScale) {
		colorScales.add(colorScale);
	}

	public Iterator<IColorScale> allColorScales() {
		return colorScales.iterator();
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
