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
package seeit3d.core.model.generator.metrics;

/**
 * This class represents a metric calculator whose values are numerical and continuous. For example the McCabe complexity.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractContinuousMetricCalculator extends MetricCalculator {

	private static final long serialVersionUID = 5441554592035195840L;

	protected static final String DEFAULT_VALUE = "0.0";

	public AbstractContinuousMetricCalculator(String name, boolean register) {
		super(name, register);
	}

	public abstract float getMaxValue();

}
