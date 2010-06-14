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
package seeit3d.modelers.xml.generator.metrics;

import org.eclipse.jdt.core.IJavaElement;

import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;
import seeit3d.general.model.generator.metrics.AbstractContinuousMetricCalculator;

/**
 * Continuous metric calculator for XML based visualization. This metric can be instantiated multiple times, one for each time it appears on the XML file
 * 
 * @author David Montaño
 * 
 */
@SeeIT3DFactoryEnabled(singleton = false)
public class XMLContinuousMetricCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = -1527870355609839773L;

	private final float maxValue;

	public XMLContinuousMetricCalculator(String name, float maxValue) {
		super(name);
		this.maxValue = maxValue;
	}

	@Override
	public float getMaxValue() {
		return maxValue;
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
		// nothing to do already calculated
		return null;
	}

	@Override
	public boolean isReplaceable() {
		return true;
	}

}
