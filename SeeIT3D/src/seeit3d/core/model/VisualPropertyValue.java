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

package seeit3d.core.model;

import static seeit3d.core.model.generator.metrics.MetricsStandardizer.*;

import java.io.Serializable;

import javax.vecmath.Color3f;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.visual.colorscale.IColorScale;

/**
 * Wrapper class to assign values to visual properties. It is used by the PolyCylinders
 * 
 * @author David Montaño
 * 
 */
public class VisualPropertyValue implements Serializable {

	private static final long serialVersionUID = 5696083743076942768L;

	private transient final SeeIT3DManager manager;

	private VisualProperty property;

	private String value;

	private MetricCalculator calculator;

	public VisualPropertyValue(VisualProperty property, String value, MetricCalculator calculator) {
		this.property = property;
		this.value = value;
		this.calculator = calculator;
		manager = SeeIT3DManager.getInstance();
	}

	public VisualProperty getProperty() {
		return property;
	}

	public Float getFloatValue() {
		float val = getValidValue();
		return val;
	}

	public Color3f getColorFromValue() {
		IColorScale colorScale = manager.getColorScale();
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
