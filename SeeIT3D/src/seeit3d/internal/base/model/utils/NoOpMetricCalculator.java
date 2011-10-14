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
package seeit3d.internal.base.model.utils;

import seeit3d.analysis.metric.AbstractNumericMetricCalculator;

import com.google.inject.Singleton;

/**
 * Metric calculator with no operation associated with it. It is used to avoid handling null references where a metric is needed
 * 
 * @author David Montaño
 * 
 */
@Singleton
public class NoOpMetricCalculator extends AbstractNumericMetricCalculator {

	private static final long serialVersionUID = 5995124210204320439L;

	public static final String NAME = "NoOp Metric";

	public NoOpMetricCalculator() {
		super(NAME);
	}

	@Override
	protected Float calculateNumericValue(Object element) {
		return 1f;
	}

}
