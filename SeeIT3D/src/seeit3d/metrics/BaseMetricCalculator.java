/**
 * Copyright (C) 2010  David Monta�o
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
package seeit3d.metrics;

import java.io.Serializable;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Parent class for all metric calculators. Provides a mechanism to register the metric with the global metric registry
 * 
 * @author David Monta�o
 * 
 */
public abstract class BaseMetricCalculator implements Serializable {

	private static final long serialVersionUID = -5439857159491897341L;

	public static final String CONTINUOUS = "CONTINUOUS";

	public static final String CATEGORIZED = "CATEGORIZED";

	private final String name;

	protected BaseMetricCalculator(String name, boolean register) {
		this.name = name;
		if (register) {
			MetricsRegistry.getInstance().registerMetric(this);
		}
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

	public abstract String calculateMetricValue(IJavaElement element);

}
