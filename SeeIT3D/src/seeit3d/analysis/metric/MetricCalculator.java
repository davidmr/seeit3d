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

import java.io.Serializable;

/**
 * Parent class for all metric calculators. Name must be unique
 * 
 * @author David Montaño
 * 
 */
public abstract class MetricCalculator implements Serializable {

	private static final long serialVersionUID = -2386735874999948525L;

	public static final String NUMERIC = "NUMERIC";

	public static final String NOMINAL = "NOMINAL";

	private final String name;

	protected MetricCalculator(String name) {
		this.name = name;
		checkAuthorizedSubclassing();
	}

	private void checkAuthorizedSubclassing() {
		boolean authorizedSubclass = false;
		if (this instanceof AbstractNominalMetricCalculator || this instanceof AbstractNumericMetricCalculator) {
			authorizedSubclass = true;
		}

		if (!authorizedSubclass) {
			throw new IllegalStateException("Direct subclassing " + this.getClass().getName() + " is no authorized. Use " + AbstractNominalMetricCalculator.class.getName() + " or "
					+ AbstractNumericMetricCalculator.class.getName());
		}
	}

	public final String name() {
		return name;
	}

	public abstract String calculate(Object element);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetricCalculator other = (MetricCalculator) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Metric [" + name + "]";
	}

}
