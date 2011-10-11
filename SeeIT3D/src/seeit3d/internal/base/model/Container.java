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
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import seeit3d.analysis.IContainerRepresentedObject;
import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.SeeIT3D;
import seeit3d.internal.base.core.api.ISeeIT3DPreferences;
import seeit3d.internal.base.error.exception.SeeIT3DException;
import seeit3d.internal.base.visual.relationships.ISceneGraphRelationshipGenerator;
import seeit3d.internal.base.visual.relationships.imp.NoRelationships;
import seeit3d.internal.utils.Utils;
import seeit3d.internal.utils.ViewConstants;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.inject.Inject;
import com.sun.j3d.utils.geometry.Box;

/**
 * Represent a container in the view, with its own mapping characteristics and represented object.
 * 
 * @author David Montaño
 * 
 */
public class Container implements Serializable, Comparable<Container> {

	private static final long serialVersionUID = 1L;

	public static final String CONTAINER_MAIN_TG = "containerMainTG";

	private transient ISeeIT3DPreferences preferences;

	private transient BranchGroup containerBG;

	private transient TransformGroup containerTG;

	private transient Switch highlighRootNode;

	private transient ISceneGraphRelationshipGenerator sceneGraphRelationshipGenerator;

	private final long identifier;

	private float width = -1;

	private float depth = -1;

	private float height = -1;

	private final IContainerRepresentedObject representedObject;

	private final List<PolyCylinder> polycylinders;

	private final List<Container> relatedContainers;

	private final List<Container> children;

	private Container parent;

	private final List<MetricCalculator> metrics;

	private final BiMap<MetricCalculator, VisualProperty> propertiesMap;

	private VisualProperty sortingProperty = ViewConstants.DEFAULT_SORTING_PROPERTY;

	private boolean sorted;

	private boolean isSelected;

	private final int currentLevel;

	private Container(IContainerRepresentedObject representedObject, List<MetricCalculator> metrics, int currentLevel) {
		this.identifier = Utils.generateContainerIdentifier();
		this.polycylinders = new ArrayList<PolyCylinder>();
		this.relatedContainers = new ArrayList<Container>();
		this.children = new ArrayList<Container>();
		this.representedObject = representedObject;
		this.metrics = metrics;
		this.propertiesMap = HashBiMap.create();
		for (int i = 0; i < VisualProperty.values().length && i < metrics.size(); i++) {
			VisualProperty prop = VisualProperty.values()[i];
			MetricCalculator metric = metrics.get(i);
			this.propertiesMap.put(metric, prop);
		}
		this.sorted = false;
		this.currentLevel = currentLevel;
		this.sceneGraphRelationshipGenerator = new NoRelationships();
		SeeIT3D.injector().injectMembers(this);
	}

	public Container(IContainerRepresentedObject representedObject, List<MetricCalculator> metrics) {
		this(representedObject, metrics, 1);
	}

	private void validateForVisualState() {
		if (highlighRootNode == null || containerBG == null || containerTG == null) {
			buildBranchGroup();
		}
	}

	private void initializePolycylinders() {
		for (PolyCylinder polyCylinder : polycylinders) {
			polyCylinder.initializePolyCylinder(propertiesMap);
		}
	}

	public int countPolyCylinders() {
		return polycylinders.size();
	}

	public Container buildContainerForPreviousLevel() {
		if (parent == null) {
			return this;
		} else {
			Vector3f position = getPosition();
			parent.setPosition(position);
			return parent;
		}
	}

	public Container buildContainerForNextLevel() {
		if (hasChildren()) {
			Container representativeContainer = children.iterator().next();
			List<MetricCalculator> newMetrics = new ArrayList<MetricCalculator>(representativeContainer.metrics);

			Container newContainer = new Container(representedObject, newMetrics, currentLevel + 1);
			newContainer.propertiesMap.clear();
			newContainer.propertiesMap.putAll(representativeContainer.propertiesMap);

			for (Container container : children) {
				newContainer.children.addAll(container.children);
				newContainer.polycylinders.addAll(container.polycylinders);
				newContainer.relatedContainers.addAll(container.relatedContainers);
			}

			newContainer.parent = this;
			// to keep location
			BranchGroup newBranchGroup = new BranchGroup();
			TransformGroup newTransformGroup = new TransformGroup();
			Transform3D transformation = new Transform3D();
			containerTG.getTransform(transformation);
			newTransformGroup.setTransform(transformation);
			newBranchGroup.addChild(newTransformGroup);
			newContainer.containerBG = newBranchGroup;

			return newContainer;
		} else {
			return this;
		}
	}

	public void updateVisualRepresentation() {
		buildBranchGroup();
	}

	private void buildBranchGroup() {

		System.err.println("Build branch group " + identifier);

		if (propertiesMap.isEmpty()) {
			throw new SeeIT3DException("In order to build the scene graph is necesary to determine the metric calculators that are going to be mapped");
		}

		if (containerBG != null) {
			containerBG.detach();
		}

		Vector3f oldPosition = getPosition();

		containerBG = new BranchGroup();
		containerBG.setCapability(BranchGroup.ALLOW_DETACH);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		TransformGroup containerTG = new TransformGroup();
		containerTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		containerTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		containerTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		containerTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		containerTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		containerTG.setUserData(this);

		resetDimensions();
		sortPolyCylinders();
		initializePolycylinders();
		calculateDimensions();

		float widestPoly = findWidestPolycylinderValue();

		float currentXPosition = -width + widestPoly;
		float currentZPosition = -depth + widestPoly;

		for (int i = 1; i <= polycylinders.size(); i++) {
			PolyCylinder poly = polycylinders.get(i - 1);
			TransformGroup polyTG = poly.getPolyCylinderTG();

			float polyHeight = poly.getHeight();
			Vector3f newPosition = new Vector3f(currentXPosition, polyHeight - height, currentZPosition);
			Utils.translateTranformGroup(polyTG, newPosition);

			if (i % preferences.getPolycylindersPerRow() == 0) {
				currentZPosition += 2 * (widestPoly + ViewConstants.POLYCYLINDER_SPACING);
				currentXPosition = -width + widestPoly;
			} else {
				currentXPosition += 2 * (widestPoly + ViewConstants.POLYCYLINDER_SPACING);
			}
			containerTG.addChild(polyTG);
		}

		if (Utils.isDebug) {
			Utils.buildGuide(containerTG);
		}

		buildHighlight(containerTG);

		if (isSelected) {
			activateHighlight();
		}

		this.containerTG = containerTG;
		containerBG.addChild(containerTG);
		setPosition(oldPosition);
	}

	private float findWidestPolycylinderValue() {
		float widestPoly = 0;

		for (PolyCylinder poly : polycylinders) {
			widestPoly = Math.max(widestPoly, poly.getWidth());
		}
		return widestPoly;
	}

	private void calculateDimensions() {
		if (isVisible()) {
			float widestPoly = findWidestPolycylinderValue();
			int polySize = polycylinders.size();
			int polyPerRow = preferences.getPolycylindersPerRow();
			if (polySize > polyPerRow) {
				width = widestPoly * polyPerRow + (ViewConstants.POLYCYLINDER_SPACING * (polyPerRow - 1));
				int rows = (int) Math.ceil((float) polySize / (float) polyPerRow);
				depth = widestPoly * rows + (ViewConstants.POLYCYLINDER_SPACING * (rows - 1));
			} else {
				width = widestPoly * polySize + (ViewConstants.POLYCYLINDER_SPACING * (polySize - 1));
				depth = widestPoly;
			}

			for (PolyCylinder poly : polycylinders) {
				float polyHeight = poly.getHeight();
				height = Math.max(height, polyHeight);
			}
		} else {
			width = 0;
			height = 0;
			depth = 0;
		}
	}

	private void resetDimensions() {
		height = -1;
		width = -1;
		depth = -1;
	}

	private void buildHighlight(TransformGroup containerTG) {
		float boxWidth = width + ViewConstants.HIGHLIGHT_PADDING;
		float boxHeight = height + ViewConstants.HIGHLIGHT_PADDING;
		float boxDepth = depth + ViewConstants.HIGHLIGHT_PADDING;
		Box surroundingBox = new Box(boxWidth, boxHeight, boxDepth, null);

		Shape3D highlightBox = Utils.buildShapeFromBox(surroundingBox, preferences.getHighlightColor());

		TransformGroup highlightBoxTG = new TransformGroup();
		highlightBoxTG.addChild(highlightBox);

		highlighRootNode = new Switch(Switch.CHILD_MASK);
		highlighRootNode.setCapability(Switch.ALLOW_SWITCH_WRITE);
		highlighRootNode.addChild(highlightBoxTG);

		containerTG.addChild(highlighRootNode);
	}

	/**
	 * Adds a new container if it is not present before
	 * 
	 * @param container
	 */
	public void addRelatedContainer(Container container) {
		boolean isPresent = false;
		for (Container related : relatedContainers) {
			if (related.identifier == container.identifier && related.representedObject.equals(representedObject)) {
				isPresent = true;
				break;
			}
		}
		if (!isPresent) {
			relatedContainers.add(container);
		}
	}

	public void addChildrenContainer(Container container) {
		if (container == null) {
			return;
		}
		boolean isPresent = false;
		for (Container child : children) {
			if (child.identifier == container.identifier && child.representedObject.equals(representedObject)) {
				isPresent = true;
				break;
			}
		}
		if (!isPresent) {
			container.parent = this;
			children.add(container);
		}
	}

	public void updateMapping(MetricCalculator metric, VisualProperty property) {
		propertiesMap.forcePut(metric, property);
		for (PolyCylinder poly : polycylinders) {
			poly.updateMapping(propertiesMap);
		}
	}

	public void removeFromMapping(MetricCalculator metric) {
		if (propertiesMap.size() > 1) {
			propertiesMap.remove(metric);
			for (PolyCylinder poly : polycylinders) {
				poly.updateMapping(propertiesMap);
			}
		}
	}

	private void sortPolyCylinders() {
		if (sorted) {
			Collections.sort(polycylinders, new Comparator<PolyCylinder>() {
				@Override
				public int compare(PolyCylinder poly1, PolyCylinder poly2) {
					VisualPropertyValue vProp1 = poly1.getVisualProperty(sortingProperty);
					VisualPropertyValue vProp2 = poly2.getVisualProperty(sortingProperty);
					Float value1 = vProp1.getFloatValue();
					Float value2 = vProp2.getFloatValue();
					return value1.compareTo(value2);
				}
			});
		}
	}

	private void activateHighlight() {
		changeSwitchNodeState(highlighRootNode, true);
	}

	private void deactiveHighlight() {
		changeSwitchNodeState(highlighRootNode, false);
	}

	private void changeSwitchNodeState(Switch node, boolean newState) {
		// do nothing if null node
		if (node != null) {
			BitSet bitSet = new BitSet(node.numChildren());
			for (int i = 0; i < bitSet.size(); i++) {
				bitSet.set(i, newState);
			}
			node.setChildMask(bitSet);
		}
	}

	public void deactivePolyCylindersHighlight() {
		for (PolyCylinder poly : polycylinders) {
			poly.setSelected(false);
		}
	}

	public void clearTransparencies() {
		for (PolyCylinder poly : polycylinders) {
			poly.clearTransparency();
		}
	}

	public List<Container> generateSceneGraphRelations() {
		return sceneGraphRelationshipGenerator.generateVisualRelationShips(this, preferences.getRelationMarkColor());
	}

	public void setPosition(Vector3f position) {
		if (containerTG != null) {
			Utils.translateTranformGroup(containerTG, position);
		}
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if (isSelected) {
			activateHighlight();
		} else {
			deactiveHighlight();
		}
	}

	public boolean isSelected() {
		return isSelected;
	}

	private boolean hasChildren() {
		return !children.isEmpty();
	}

	public boolean hasPolycylinder(PolyCylinder polycylinder) {
		return polycylinders.contains(polycylinder);
	}

	public BranchGroup getContainerBG() {
		validateForVisualState();
		return containerBG;
	}

	public TransformGroup getTransformGroup() {
		validateForVisualState();
		return containerTG;
	}

	public List<MetricCalculator> getMetrics() {
		return Collections.unmodifiableList(metrics);
	}

	public float getWidth() {
		if (width == -1) {
			throw new IllegalStateException("Width has not been calculated");
		}
		return width;
	}

	public float getDepth() {
		if (depth == -1) {
			throw new IllegalStateException("Depth has not been calculated");
		}
		return depth;
	}

	public float getHeight() {
		if (height == -1) {
			throw new IllegalStateException("Height has not been calculated");
		}
		return height;
	}

	public Vector3f getPosition() {
		if (containerBG == null || containerTG == null) {
			return new Vector3f();
		} else {
			Transform3D transformation = new Transform3D();
			containerTG.getTransform(transformation);
			Vector3f position = new Vector3f();
			transformation.get(position);
			return position;
		}
	}

	public BiMap<MetricCalculator, VisualProperty> getPropertiesMap() {
		return propertiesMap;
	}

	public List<Container> getRelatedContainers() {
		return relatedContainers;
	}

	public List<Container> getRelatedContainersToHide() {
		if (sceneGraphRelationshipGenerator == null || sceneGraphRelationshipGenerator instanceof NoRelationships) {
			return relatedContainers;
		} else {
			return Collections.emptyList();
		}
	}

	public List<Container> getRelatedContainersToShow() {
		if (sceneGraphRelationshipGenerator == null || sceneGraphRelationshipGenerator instanceof NoRelationships) {
			return Collections.emptyList();
		} else {
			return relatedContainers;
		}
	}

	public String getName() {
		return representedObject.getName();
	}

	public String getGranularityLevelName() {
		return representedObject.granularityLevelName(currentLevel);
	}

	private boolean isVisible() {
		return !polycylinders.isEmpty();
	}

	public void setSortingProperty(VisualProperty visualProperty) {
		sortingProperty = visualProperty;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public void setSceneGraphRelationshipGenerator(ISceneGraphRelationshipGenerator sceneGraphRelationshipGenerator) {
		if (!relatedContainers.isEmpty()) {
			if (this.sceneGraphRelationshipGenerator != null) {
				this.sceneGraphRelationshipGenerator.unused();
			}
			this.sceneGraphRelationshipGenerator = sceneGraphRelationshipGenerator;
			this.sceneGraphRelationshipGenerator.initialize();
		}
	}

	public ISceneGraphRelationshipGenerator getSceneGraphRelationshipGenerator() {
		return sceneGraphRelationshipGenerator;
	}

	public void addPolyCylinder(PolyCylinder polyCylinder) {
		polycylinders.add(polyCylinder);
	}

	public List<PolyCylinder> getPolycylinders() {
		return polycylinders;
	}

	public Container toContainer(PolyCylinder polycylinder) {
		String name = polycylinder.getName();
		for (Container container : children) {
			if (name.equals(container.getName())) {
				return container;
			}
		}
		return null;
	}

	@Override
	public int compareTo(Container o) {
		return new Long(identifier).compareTo(new Long(o.identifier));
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
		Container other = (Container) obj;
		if (identifier != other.identifier) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getName() + ": mapping " + propertiesMap;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		SeeIT3D.injector().injectMembers(this);
		sceneGraphRelationshipGenerator = new NoRelationships();
	}

	@Inject
	public void setPreferences(ISeeIT3DPreferences preferences) {
		this.preferences = preferences;
	}


}
