package seeit3d.model.representation;

import static seeit3d.utils.MetricsStandardizer.*;

import java.io.Serializable;

import javax.vecmath.Color3f;

import seeit3d.colorscale.IColorScale;
import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;

/**
 * Wrapper class to assign values to visual properties. It is used by the PolyCylinders
 * 
 * @author David
 * 
 */
public class VisualPropertyValue implements Serializable {

	private static final long serialVersionUID = 5696083743076942768L;

	private transient final SeeIT3DManager manager;

	private VisualProperty property;

	private String value;

	private BaseMetricCalculator calculator;

	public VisualPropertyValue(VisualProperty property, String value, BaseMetricCalculator calculator) {
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
