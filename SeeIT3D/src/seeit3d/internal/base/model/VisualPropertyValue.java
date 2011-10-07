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
package seeit3d.internal.base.model;

import static seeit3d.internal.base.analysis.metrics.MetricsNormalizer.getStandardizedValueFromMetric;

import java.io.Serializable;

import javax.vecmath.Color3f;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.visual.colorscale.IColorScale;

/**
 * Wrapper class to assign values to visual properties. It is used by the PolyCylinders
 * 
 * @author David Montaño
 * 
 */
public class VisualPropertyValue implements Serializable {

	private static final long serialVersionUID = 5696083743076942768L;

	private transient final IColorScale colorScale;

	private final VisualProperty property;

	private final String value;

	private final MetricCalculator calculator;

	public VisualPropertyValue(VisualProperty property, String value, MetricCalculator calculator, IColorScale colorScale) {
		this.property = property;
		this.value = value;
		this.calculator = calculator;
		this.colorScale = colorScale;
	}

	public VisualProperty getProperty() {
		return property;
	}

	public Float getFloatValue() {
		float val = getValidValue();
		return val;
	}

	public Color3f getColorFromValue() {
		float val = getValidValue();
		return colorScale.generate(val);
	}

	private float getValidValue() {
		float standardized = getStandardizedValueFromMetric(calculator, value);
		float correctValue = Math.max(property.getMinValue(), standardized);
		return correctValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (property == null ? 0 : property.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		VisualPropertyValue other = (VisualPropertyValue) obj;
		if (property == null) {
			if (other.property != null) {
				return false;
			}
		} else if (!property.equals(other.property)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return property + " - " + value;
	}

}
