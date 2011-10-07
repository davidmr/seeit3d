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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import seeit3d.analysis.IEclipseResourceRepresentation;
import seeit3d.analysis.NoEclipseResourceRepresentation;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.SeeIT3D;
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

	private final List<VisualPropertyValue> visualPropertyValues;

	private final Map<MetricCalculator, String> metricsValues;

	private final BiMap<MetricCalculator, VisualProperty> propertiesMap;

	private Color3f baseColor;

	private boolean selected;

	public PolyCylinder(String name, Map<MetricCalculator, String> metricsValues, IEclipseResourceRepresentation representation) {
		this.name = name;
		this.metricsValues = metricsValues;
		this.representation = representation;
		visualPropertyValues = new ArrayList<VisualPropertyValue>();
		identifier = Utils.generatePolyCylinderIdentifier();
		propertiesMap = HashBiMap.create();
		SeeIT3D.injector().injectMembers(this);
	}

	public PolyCylinder(String name, Map<MetricCalculator, String> metricsValues) {
		this(name, metricsValues, NoEclipseResourceRepresentation.INSTANCE);
	}

	public void initializePolyCylinder(BiMap<MetricCalculator, VisualProperty> propertiesMap) {
		updateMapping(propertiesMap);
		updateState();
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

	private void updateState() {

		polyCylinderTG = new TransformGroup();

		updateMetricValues();

		float height = getHeight();
		float width = getWidth();

		VisualPropertyValue visualProperty = getVisualProperty(VisualProperty.COLOR);
		baseColor = visualProperty.getColorFromValue();

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

	private void updateMetricValues() {
		// assign values to visual properties
		visualPropertyValues.clear();
		for (VisualProperty visualProperty : VisualProperty.values()) {
			MetricCalculator metric = propertiesMap.inverse().get(visualProperty);
			String value = metricsValues.get(metric) != null ? metricsValues.get(metric) : Float.toString(visualProperty.getDefaultValue());
			IColorScale currentColorScale = seeIT3DVisualProperties.getCurrentColorScale();
			visualPropertyValues.add(new VisualPropertyValue(visualProperty, value, metric, currentColorScale));
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
		float height = getVisualProperty(VisualProperty.HEIGHT).getFloatValue();
		return height / HEIGHT_SCALING_FACTOR;
	}

	public float getWidth() {
		float value = getVisualProperty(VisualProperty.CROSS_SECTION).getFloatValue();
		return value / WIDTH_REDUCTION_FACTOR;
	}

	public VisualPropertyValue getVisualProperty(VisualProperty prop) {
		for (VisualPropertyValue vprop : visualPropertyValues) {
			if (vprop.getProperty().equals(prop)) {
				return vprop;
			}
		}
		return null;
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
