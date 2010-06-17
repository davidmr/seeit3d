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
package seeit3d.general.bus.events;

import seeit3d.general.bus.IEvent;
import seeit3d.general.model.VisualProperty;
import seeit3d.general.model.generator.metrics.MetricCalculator;

/**
 * Event triggered when the mapping between visual property and metric is changed
 * 
 * @author David Montaño
 * 
 */
public class MappingChangedEvent implements IEvent {

	private final VisualProperty visualProperty;

	private final MetricCalculator metric;

	public MappingChangedEvent(VisualProperty visualProperty, MetricCalculator metric) {
		this.visualProperty = visualProperty;
		this.metric = metric;
	}

	public VisualProperty getVisualProperty() {
		return visualProperty;
	}

	public MetricCalculator getMetricCalculator() {
		return metric;
	}

}
