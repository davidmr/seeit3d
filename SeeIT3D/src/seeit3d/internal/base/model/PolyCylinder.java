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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import seeit3d.analysis.IEclipseResourceRepresentation;
import seeit3d.analysis.NoEclipseResourceRepresentation;
import seeit3d.analysis.metric.AbstractNominalMetricCalculator;
import seeit3d.analysis.metric.AbstractNumericMetricCalculator;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.SeeIT3D;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.model.utils.NoOpMetricCalculator;
import seeit3d.internal.base.visual.api.ISeeIT3DVisualProperties;
import seeit3d.internal.base.visual.colorscale.IColorScale;
import seeit3d.internal.utils.Utils;
import seeit3d.internal.utils.ViewConstants;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.sun.j3d.utils.geometry.Box;

/**
 * Defines a polycylinder that represents a mapped object
 * 
 * @author David Montaño
 * 
 */
public class PolyCylinder implements Serializable {

	private static final long serialVersionUID = -3486896587576559868L;

	private static final float WIDTH_REDUCTION_FACTOR = 1f;

	private static final float HEIGHT_SCALING_FACTOR = 0.3f;

	private transient ISeeIT3DVisualProperties seeIT3DVisualProperties;

	private transient ISeeIT3DPreferences preferences;

	private transient TransformGroup polyCylinderTG;

	private transient Material boxColor;

	private transient TransparencyAttributes transparency;

	private transient IEclipseResourceRepresentation representation;

	private final long identifier;

	private final String name;

	private final Map<MetricCalculator, Float> normalizedMetricValues;

	private final Map<MetricCalculator, String> metricsValues;

	private final BiMap<MetricCalculator, VisualProperty> propertiesMap;

	private Color3f baseColor;

	private boolean selected;

	public PolyCylinder(String name, Map<MetricCalculator, String> metricsValues, IEclipseResourceRepresentation representation) {
		this.name = name;
		this.metricsValues = metricsValues;
		this.representation = representation;
		identifier = Utils.generatePolyCylinderIdentifier();
		propertiesMap = HashBiMap.create();
		normalizedMetricValues = new HashMap<MetricCalculator, Float>();
		SeeIT3D.injector().injectMembers(this);
	}

	public PolyCylinder(String name, Map<MetricCalculator, String> metricsValues) {
		this(name, metricsValues, NoEclipseResourceRepresentation.INSTANCE);
	}

	public void changeTransparency(boolean moreTransparent) {
		if (transparency != null) {
			float currentTransparency = transparency.getTransparency();
			float newTransparency = 0.0f;
			if (moreTransparent) {
				newTransparency = currentTransparency + preferences.getTransparencyStep();
			} else {
				newTransparency = currentTransparency - preferences.getTransparencyStep();
			}

			if (newTransparency >= 0.0f && newTransparency <= 1.0f) {
				transparency.setTransparency(newTransparency);
			}

		}
	}

	public void clearTransparency() {
		if (transparency != null) {
			transparency.setTransparency(0.0f);
		}
	}

	public void updateMapping(BiMap<MetricCalculator, VisualProperty> newPropertiesMap) {
		propertiesMap.clear();
		propertiesMap.putAll(newPropertiesMap);
		MetricCalculator noOpMetric = SeeIT3D.injector().getInstance(NoOpMetricCalculator.class);
		for (VisualProperty visualProperty : VisualProperty.values()) {
			MetricCalculator metric = propertiesMap.inverse().get(visualProperty);
			if (metric == null) {
				propertiesMap.put(noOpMetric, visualProperty);
			}
		}
	}

	public void updateState() {

		polyCylinderTG = new TransformGroup();

		float height = getHeight();
		float width = getWidth();

		IColorScale colorScale = seeIT3DVisualProperties.getCurrentColorScale();
		float colorValue = getNormalizedValueForVisualProperty(VisualProperty.COLOR);
		baseColor = colorScale.generate(colorValue);

		Appearance app = new Appearance();

		Color3f lighterColor = new Color3f(0.2f, 0.2f, 0.2f);

		boxColor = new Material(ViewConstants.BLACK, ViewConstants.BLACK, baseColor, lighterColor, 64.0f);
		boxColor.setLightingEnable(true);
		boxColor.setCapability(Material.ALLOW_COMPONENT_WRITE);

		app.setMaterial(boxColor);

		transparency = new TransparencyAttributes(TransparencyAttributes.SCREEN_DOOR, 0.0f);
		transparency.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		transparency.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
		app.setTransparencyAttributes(transparency);

		Box box = new Box(width, height, width, app);
		box.setUserData(this);

		polyCylinderTG.addChild(box);
	}

	public float getNormalizedValueForVisualProperty(VisualProperty visualProperty) {
		MetricCalculator metricCalculator = propertiesMap.inverse().get(visualProperty);
		Float value = normalizedMetricValues.get(metricCalculator);
		return Math.max(value != null ? value : 0, visualProperty.getMinValue());
	}

	public void normalize(Map<AbstractNumericMetricCalculator, Float> maxValues, Map<AbstractNominalMetricCalculator, Map<String, Float>> nominalValues) {
		normalizedMetricValues.clear();

		for (Map.Entry<MetricCalculator, String> entry : metricsValues.entrySet()) {
			MetricCalculator calculator = entry.getKey();
			String value = entry.getValue();
			if (calculator instanceof AbstractNumericMetricCalculator) {
				Float maxValue = maxValues.get(calculator);
				Float floatValue = Float.parseFloat(value);
				float normalized = maxValue.floatValue() > 0 ? (floatValue.floatValue() / maxValue.floatValue()) : 0;
				normalizedMetricValues.put(calculator, normalized);
			}
			if (calculator instanceof AbstractNominalMetricCalculator) {
				Map<String, Float> valuesForNominalMetricCalculator = nominalValues.get(calculator);
				Float floatValue = valuesForNominalMetricCalculator.get(value);
				normalizedMetricValues.put(calculator, floatValue);
			}
		}
	}

	private void activateHighlight() {
		if (boxColor != null) {
			boxColor.setDiffuseColor(preferences.getHighlightColor());
		}
	}

	private void deactivateHighlight() {
		if (boxColor != null) {
			boxColor.setDiffuseColor(baseColor);
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected) {
			activateHighlight();
		} else {
			deactivateHighlight();
		}
	}

	public String getName() {
		return name;
	}

	public Map<MetricCalculator, String> getMetricsValues() {
		return metricsValues;
	}

	public boolean isSelected() {
		return selected;
	}

	public IEclipseResourceRepresentation getRepresentation() {
		return representation;
	}

	public float getHeight() {
		float height = getNormalizedValueForVisualProperty(VisualProperty.HEIGHT);
		return height / HEIGHT_SCALING_FACTOR;
	}

	public float getWidth() {
		float value = getNormalizedValueForVisualProperty(VisualProperty.CROSS_SECTION);
		return value / WIDTH_REDUCTION_FACTOR;
	}

	public TransformGroup getPolyCylinderTG() {
		if (polyCylinderTG == null) {
			updateState();
		}
		return polyCylinderTG;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (identifier ^ identifier >>> 32);
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
		PolyCylinder other = (PolyCylinder) obj;
		if (identifier != other.identifier) {
			return false;
		}
		return true;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		SeeIT3D.injector().injectMembers(this);
	}

	@Inject
	public void setSeeIT3DVisualProperties(ISeeIT3DVisualProperties seeIT3DVisualProperties) {
		this.seeIT3DVisualProperties = seeIT3DVisualProperties;
	}

	@Inject
	public void setPreferences(ISeeIT3DPreferences preferences) {
		this.preferences = preferences;
	}

}
