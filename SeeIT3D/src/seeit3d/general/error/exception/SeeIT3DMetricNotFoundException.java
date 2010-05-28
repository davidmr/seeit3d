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
package seeit3d.general.error.exception;

/**
 * Exception to indicate that a specific metric was not found in the general metric registry
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DMetricNotFoundException extends SeeIT3DException {

	private static final long serialVersionUID = 1L;

	public SeeIT3DMetricNotFoundException(String metricName) {
		super("Metric with name '" + metricName + "' not found in registry. Please register it before using the metric");

	}
}
