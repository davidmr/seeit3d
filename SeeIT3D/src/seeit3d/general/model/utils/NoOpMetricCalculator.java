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
package seeit3d.general.model.utils;

import org.eclipse.jdt.core.IJavaElement;

import seeit3d.general.model.factory.annotations.SeeIT3DFactoryEnabled;
import seeit3d.general.model.generator.metrics.AbstractContinuousMetricCalculator;

/**
 * Metric calculator with no operation associated with it. It is used to avoid handling null references where a metric is needed. This metric must be manually registered
 * 
 * @author David Montaño
 * 
 */
@SeeIT3DFactoryEnabled(singleton = true)
public class NoOpMetricCalculator extends AbstractContinuousMetricCalculator {

	private static final long serialVersionUID = 5995124210204320439L;

	public static final String NAME = "NoOp Metric";

	public NoOpMetricCalculator() {
		super(NAME);
	}

	@Override
	public float getMaxValue() {
		return 1;
	}

	@Override
	public String calculateMetricValue(IJavaElement element) {
		return "1";
	}

	@Override
	public boolean isReplaceable() {
		return false;
	}

}
