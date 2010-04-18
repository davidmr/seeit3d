package seeit3d.model.representation;

import java.io.*;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.*;
import seeit3d.model.EclipseResourceRepresentation;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sun.j3d.utils.geometry.Box;

/**
 * Defines a polycylinder that represents a mapped object
 * 
 * @author David
 * 
 */
public class PolyCylinder implements Serializable {

	private static final long serialVersionUID = -3486896587576559868L;

	private static final float WIDTH_REDUCTION_FACTOR = 1f;

	private static final float HEIGHT_SCALING_FACTOR = 0.3f;

	private transient TransformGroup polyCylinderTG;

	private transient SeeIT3DManager manager;

	private final long identifier;

	private final List<VisualPropertyValue> visualPropertyValues;

	private final Map<BaseMetricCalculator, String> metricsValues;

	private final BiMap<BaseMetricCalculator, VisualProperty> propertiesMap;

	private transient Material boxColor;

	private transient TransparencyAttributes transparency;

	private Color3f baseColor;

	private transient EclipseResourceRepresentation representation;

	private boolean selected;

	public PolyCylinder(Map<BaseMetricCalculator, String> metricsValues, EclipseResourceRepresentation representation) {
		this.metricsValues = metricsValues;
		this.representation = representation;
		visualPropertyValues = new ArrayList<VisualPropertyValue>();
		identifier = Utils.generatePolyCylinderIdentifier();
		manager = SeeIT3DManager.getInstance();
		propertiesMap = HashBiMap.create();
	}

	public PolyCylinder(Map<BaseMetricCalculator, String> metricsValues) {
		this(metricsValues, new NoEclipseRepresentation());
	}

	public VisualPropertyValue getVisualProperty(VisualProperty prop) {
		for (VisualPropertyValue vprop : visualPropertyValues) {
			if (vprop.getProperty().equals(prop)) {
				return vprop;
			}
		}
		return null;
	}

	public void initializePolyCylinder(BiMap<BaseMetricCalculator, VisualProperty> propertiesMap) {
		updateMapping(propertiesMap);
		updateState();
	}

	public synchronized TransformGroup getPolyCylinderTG() {
		if (polyCylinderTG == null) {
			updateState();
		}
		return polyCylinderTG;
	}

	private void updateState() {

		polyCylinderTG = new TransformGroup();

		updateMetricValues();

		float height = getHeight();
		float width = getWidth();

		VisualPropertyValue visualProperty = getVisualProperty(VisualProperty.COLOR);
		baseColor = visualProperty.getColorFromValue();

		Appearance app = new Appearance();

		Color3f lighterColor = new Color3f(0.5f, 0.5f, 0.5f);

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
			BaseMetricCalculator metric = propertiesMap.inverse().get(visualProperty);
			String value = metricsValues.get(metric) != null ? metricsValues.get(metric) : Float.toString(visualProperty.getDefaultValue());
			visualPropertyValues.add(new VisualPropertyValue(visualProperty, value, metric));
		}
	}

	public PolyCylinder createCopy() {
		Map<BaseMetricCalculator, String> newMetricValues = new HashMap<BaseMetricCalculator, String>(metricsValues);
		PolyCylinder newPoly = new PolyCylinder(newMetricValues, representation);
		return newPoly;
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

	private void activateHighlight() {
		if (boxColor != null) {
			boxColor.setDiffuseColor(manager.getHighlightColor());
		}
	}

	private void deactivateHighlight() {
		if (boxColor != null) {
			boxColor.setDiffuseColor(baseColor);
		}
	}

	public EclipseResourceRepresentation getRepresentation() {
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

	public void changeTransparency(boolean moreTransparent) {
		if (transparency != null) {
			float currentTransparency = transparency.getTransparency();
			float newTransparency = 0.0f;
			if (moreTransparent) {
				newTransparency = currentTransparency + manager.getTransparencyStep();
			} else {
				newTransparency = currentTransparency - manager.getTransparencyStep();
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

	public void updateMapping(BiMap<BaseMetricCalculator, VisualProperty> newPropertiesMap) {
		propertiesMap.clear();
		propertiesMap.putAll(newPropertiesMap);
		BaseMetricCalculator noOpMetric = MetricsRegistry.getInstance().getMetric(NoOpMetricCalculator.NAME);
		for (VisualProperty visualProperty : VisualProperty.values()) {
			BaseMetricCalculator metric = propertiesMap.inverse().get(visualProperty);
			if (metric == null) {
				propertiesMap.put(noOpMetric, visualProperty);
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		manager = SeeIT3DManager.getInstance();
		representation = new NoEclipseRepresentation();
	}

}
