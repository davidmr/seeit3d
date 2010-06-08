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

import java.util.List;

/**
 * This class represents a metric calculator whose values are categories. For example a control structure metric with values for, if, while, etc.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractCategorizedMetricCalculator extends MetricCalculator {

	private static final long serialVersionUID = 3242755411010446121L;

	public AbstractCategorizedMetricCalculator(String name, boolean register) {
		super(name, register);
	}

	public abstract int getNumCategories();

	public abstract List<String> getCategories();

}
