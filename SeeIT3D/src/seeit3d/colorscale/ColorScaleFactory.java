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

import java.util.ArrayList;
import java.util.List;

import seeit3d.colorscale.imp.*;

/**
 * Factory class to provide access to the set of all color scales in the application.
 * 
 * @author David Montaño
 * 
 */
public class ColorScaleFactory {

	private static final ArrayList<IColorScale> allColorScales;

	static {
		allColorScales = newArrayList(
				new BlueTone(),
				new BlueToYellow(),
				new ColdToHotColorScale(),
				new GrayColorScale(), 
				new HeatedObject(),
				new LinearOptimal(), 
				new MagentaTone(),
				new Rainbow());
	}

	public static List<IColorScale> createAllColorScales() {
		return allColorScales;
	}

	public static IColorScale findByName(String colorScaleName) {
		for (IColorScale colorScale : allColorScales) {
			if (colorScale.getName().equals(colorScaleName)) {
				return colorScale;
			}
		}
		return null;
	}
}
