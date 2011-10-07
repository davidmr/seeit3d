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
package seeit3d.analysis.metric;

import java.util.List;

/**
 * This class represents a metric calculator whose values are categories. For example a control structure metric with values for, if, while, etc.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractNominalMetricCalculator extends MetricCalculator {

	private static final long serialVersionUID = 3242755411010446121L;

	public AbstractNominalMetricCalculator(String name) {
		super(name);
	}

	public abstract List<String> getCategories();

}
