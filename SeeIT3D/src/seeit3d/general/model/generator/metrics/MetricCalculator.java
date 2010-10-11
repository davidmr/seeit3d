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
package seeit3d.general.model.generator.metrics;

import java.io.Serializable;

/**
 * Parent class for all metric calculators.
 * 
 * @author David Montaño
 * 
 */
public abstract class MetricCalculator implements Serializable {

	private static final long serialVersionUID = -5439857159491897341L;

	public static final String CONTINUOUS = "CONTINUOUS";

	public static final String CATEGORIZED = "CATEGORIZED";

	private final String name;

	protected MetricCalculator(String name) {
		this.name = name;
		checkAuthorizedSubclassing();
	}

	private void checkAuthorizedSubclassing() {
		boolean authorizedSubclass = false;
		if (this instanceof AbstractCategorizedMetricCalculator || this instanceof AbstractContinuousMetricCalculator) {
			authorizedSubclass = true;
		}

		if (!authorizedSubclass) {
			throw new IllegalStateException("Direct subclassing " + this.getClass().getName() + " is no authorized. Use " + AbstractCategorizedMetricCalculator.class.getName() + " or "
					+ AbstractContinuousMetricCalculator.class.getName());
		}
	}

	public final String getMetricName() {
		return name;
	}

	public abstract boolean isReplaceable();

	public abstract String calculateMetricValue(Object element);

}
