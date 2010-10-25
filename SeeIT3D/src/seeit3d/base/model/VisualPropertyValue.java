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

package seeit3d.base.model;

import static seeit3d.base.model.generator.metrics.MetricsNormalizer.*;

import java.io.Serializable;

import javax.vecmath.Color3f;

import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.base.visual.colorscale.IColorScale;

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
		return colorScale.generateCuantitavieColor(val);
	}

	private float getValidValue() {
		float standardized = getStandardizedValueFromMetric(calculator, value);
		float correctValue = Math.max(property.getMinValue(), standardized);
		return correctValue;
	}

	public String getRealValue() {
		return value;
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