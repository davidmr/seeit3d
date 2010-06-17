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

package seeit3d.general.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import seeit3d.general.SeeIT3DAPILocator;
import seeit3d.general.model.factory.SeeIT3DFactory;
import seeit3d.general.model.generator.metrics.MetricCalculator;
import seeit3d.general.model.utils.NoEclipseRepresentation;
import seeit3d.general.model.utils.NoOpMetricCalculator;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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

	private transient TransformGroup polyCylinderTG;

	private transient Preferences preferences;

	private transient Material boxColor;

	private transient TransparencyAttributes transparency;

	private transient IEclipseResourceRepresentation representation;

	private final long identifier;

	private final List<VisualPropertyValue> visualPropertyValues;

	private final Map<MetricCalculator, String> metricsValues;

	private final BiMap<MetricCalculator, VisualProperty> propertiesMap;

	private Color3f baseColor;

	private boolean selected;

	public PolyCylinder(Map<MetricCalculator, String> metricsValues, IEclipseResourceRepresentation representation) {
		this.metricsValues = metricsValues;
		this.representation = representation;
		visualPropertyValues = new ArrayList<VisualPropertyValue>();
		identifier = Utils.generatePolyCylinderIdentifier();
		propertiesMap = HashBiMap.create();
		preferences = SeeIT3DAPILocator.findPreferences();
	}

	public PolyCylinder(Map<MetricCalculator, String> metricsValues) {
		this(metricsValues, new NoEclipseRepresentation());
	}

	public void initializePolyCylinder(BiMap<MetricCalculator, VisualProperty> propertiesMap) {
		updateMapping(propertiesMap);
		updateState();
	}

	public PolyCylinder createCopy() {
		Map<MetricCalculator, String> newMetricValues = new HashMap<MetricCalculator, String>(metricsValues);
		PolyCylinder newPoly = new PolyCylinder(newMetricValues, representation);
		return newPoly;
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
		MetricCalculator noOpMetric = SeeIT3DFactory.getInstance(NoOpMetricCalculator.class);
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
			visualPropertyValues.add(new VisualPropertyValue(visualProperty, value, metric));
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

	public synchronized TransformGroup getPolyCylinderTG() {
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
		preferences = SeeIT3DAPILocator.findPreferences();
		representation = new NoEclipseRepresentation();
	}

}
