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

import seeit3d.internal.base.error.exception.SeeIT3DException;

/**
 * This class represents a metric calculator whose values are numerical and continuous. For example the McCabe complexity.
 * 
 * @author David Montaño
 * 
 */
public abstract class AbstractNumericMetricCalculator extends MetricCalculator {

	private static final long serialVersionUID = 5441554592035195840L;

	protected static final Float DEFAULT_VALUE = 0f;

	public AbstractNumericMetricCalculator(String name) {
		super(name);
	}

	@Override
	public final String calculate(Object element) {
		Float value = calculateNumericValue(element);
		if (value.isInfinite() || value.isNaN()) {
			throw new SeeIT3DException("Value " + value + " in metric " + name() + " is not a valid number");
		}
		return value.toString();
	}

	protected abstract Float calculateNumericValue(Object element);

}
