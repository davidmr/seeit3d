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
package seeit3d.internal.base.visual.colorscale;

import java.util.ArrayList;
import java.util.List;

import seeit3d.internal.base.visual.api.IColorScaleRegistry;

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
	public void registerColorScale(IColorScale colorScale) {
		colorScales.add(colorScale);
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
