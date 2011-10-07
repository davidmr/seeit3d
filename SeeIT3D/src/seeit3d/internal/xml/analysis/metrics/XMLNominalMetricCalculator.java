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
package seeit3d.internal.xml.analysis.metrics;

import java.util.List;

import seeit3d.analysis.metric.AbstractNominalMetricCalculator;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.xml.internal.EntryMetricValue;
import seeit3d.internal.xml.internal.MetricsValue;
import seeit3d.internal.xml.internal.Polycylinder;

/**
 * Categorized metric calculator for XML based visualization
 * 
 * @author David Montaño
 * 
 */
public class XMLNominalMetricCalculator extends AbstractNominalMetricCalculator {

	private static final long serialVersionUID = -7574154133744003269L;

	private final List<String> categories;

	public XMLNominalMetricCalculator(String name, List<String> categories) {
		super(name);
		this.categories = categories;
	}

	@Override
	public List<String> getCategories() {
		return categories;
	}

	@Override
	public String calculate(Object element) {
		Polycylinder polyXML = (Polycylinder) element;
		MetricsValue metricsValue = polyXML.getMetricsValue();
		List<EntryMetricValue> entryMetricValues = metricsValue.getEntryMetricValue();
		for (EntryMetricValue entryMetricValue : entryMetricValues) {
			if (entryMetricValue.getMetricName().equals(this.name())) {
				return entryMetricValue.getValue();
			}
		}
		throw new SeeIT3DException("XML metric not found");
	}
}
