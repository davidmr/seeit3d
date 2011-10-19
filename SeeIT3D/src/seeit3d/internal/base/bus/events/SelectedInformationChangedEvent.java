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
package seeit3d.internal.base.bus.events;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * Event triggered when the underlying selected information in the visualization area is changed
 * 
 * @author David Montaño
 * 
 */
public class SelectedInformationChangedEvent implements IEvent {

	private final Iterable<Container> selectedContainers;

	private final Iterable<PolyCylinder> selectedPolycylinders;

	public SelectedInformationChangedEvent(Iterable<Container> selectedContainers, Iterable<PolyCylinder> selectedPolycylinders) {
		this.selectedContainers = selectedContainers;
		this.selectedPolycylinders = selectedPolycylinders;
	}

	public Map<String, String> getCurrentMetricsValuesFromSelection() {
		boolean multipleSelection = isMultiplePolycylinderSelection();
		Map<String, String> currentMetricsValuesFromSelection = new HashMap<String, String>();
		if (multipleSelection) {
			currentMetricsValuesFromSelection.put("Multiple selection", "-");
		} else {
			PolyCylinder selected = selectedPolycylinders.iterator().next();
			if (selected != null) {
				Map<MetricCalculator, String> metricsValues = selected.getMetricsValues();
				for (Map.Entry<MetricCalculator, String> entry : metricsValues.entrySet()) {
					currentMetricsValuesFromSelection.put(entry.getKey().name(), entry.getValue());
				}
			}
		}
		return currentMetricsValuesFromSelection;
	}

	private boolean isMultiplePolycylinderSelection() {
		Iterator<PolyCylinder> iterator = selectedPolycylinders.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			return iterator.hasNext();
		}
		return false;
	}

	public Collection<String> metricNamesInMapping() {
		if (isMultiplePolycylinderSelection() || !isContainerSelected()) {
			return Collections.emptyList();
		} else {
			Container container = selectedContainers.iterator().next();
			return Collections2.transform(container.getPropertiesMap().keySet(), new Function<MetricCalculator, String>() {
				@Override
				public String apply(MetricCalculator metric) {
					return metric.name();
				}
			});
		}
	}

	public String getSelectedContainersName() {
		StringBuilder builder = new StringBuilder();
		for (Container container : selectedContainers) {
			builder.append(container.getName());
			builder.append(", ");
		}
		return builder.substring(0, Math.max(builder.length() - 2, 0));
	}

	public String getSelectedPolycylindersName() {
		StringBuilder builder = new StringBuilder();
		for (PolyCylinder poly : selectedPolycylinders) {
			builder.append(poly.getName());
			builder.append(", ");
		}
		return builder.substring(0, Math.max(builder.length() - 2, 0));
	}

	public boolean isContainerSelected() {
		return selectedContainers.iterator().next() != null;
	}

}
