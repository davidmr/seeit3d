package seeit3d.model.representation;

import java.io.*;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.error.exception.SeeIT3DException;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.metrics.MetricsRegistry;
import seeit3d.model.ContainerRepresentedObject;
import seeit3d.preferences.Preferences;
import seeit3d.utils.Utils;
import seeit3d.utils.ViewConstants;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.sun.j3d.utils.geometry.Box;

/**
 * Represent a container in the view, with its own mapping characteristics
 * 
 * @author David
 * 
 */
public class Container implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Preferences preferences;

	private final long identifier;

	private float width = -1;

	private float depth = -1;

	private float height = -1;

	private final ContainerRepresentedObject representedObject;

	private final List<PolyCylinder> polycylinders;

	private final List<Container> relatedContainers;

	private final List<Container> children;

	private Container parent;

	private final List<BaseMetricCalculator> metrics;

	private final BiMap<BaseMetricCalculator, VisualProperty> propertiesMap;

	private transient BranchGroup containerBG;

	private transient Switch highlighRootNode;

	private transient Switch relationMarkNode;

	private VisualProperty sortingProperty;

	private boolean sorted;

	private boolean isSelected;

	private final int currentLevel;

	private Container(ContainerRepresentedObject representedObject, List<BaseMetricCalculator> metrics, int currentLevel) {
		checkMetricsValidity(metrics);
		identifier = Utils.generateContainerIdentifier();
		preferences = Preferences.getInstance();
		polycylinders = new ArrayList<PolyCylinder>();
		relatedContainers = new ArrayList<Container>();
		children = new ArrayList<Container>();
		this.representedObject = representedObject;
		this.metrics = metrics;
		this.propertiesMap = HashBiMap.create();
		for (int i = 0; i < VisualProperty.values().length && i < metrics.size(); i++) {
			VisualProperty prop = VisualProperty.values()[i];
			BaseMetricCalculator metric = metrics.get(i);
			this.propertiesMap.put(metric, prop);
		}
		sorted = false;
		this.currentLevel = currentLevel;
	}

	public Container(ContainerRepresentedObject representedObject, List<BaseMetricCalculator> metrics) {
		this(representedObject, metrics, 1);
	}

	private void checkMetricsValidity(List<BaseMetricCalculator> metricsToEvaluate) {
		MetricsRegistry registry = MetricsRegistry.getInstance();
		for (BaseMetricCalculator metric : metricsToEvaluate) {
			registry.checkMetricRegistered(metric.getMetricName());
		}
	}

	public Container buildContainerForPreviousLevel() {
		if (parent == null) {
			return this;
		} else {
			Vector3f position = extractPosition();
			parent.setPosition(position);
			return parent;
		}
	}

	public void setPosition(Vector3f position) {
		if (containerBG != null) {
			TransformGroup child = (TransformGroup) containerBG.getChild(0);
			Utils.translateTranformGroup(child, position);
		}
	}

	public Container buildContainerForNextLevel() {
		if (hasChildren()) {
			Container representativeContainer = children.iterator().next();
			List<BaseMetricCalculator> newMetrics = new ArrayList<BaseMetricCalculator>(representativeContainer.metrics);

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
			((TransformGroup) containerBG.getChild(0)).getTransform(transformation);
			newTransformGroup.setTransform(transformation);
			newBranchGroup.addChild(newTransformGroup);
			newContainer.containerBG = newBranchGroup;

			return newContainer;
		} else {
			return this;
		}
	}

	public void autoReference() {
		parent = this;
		children.add(this);
	}

	private boolean hasChildren() {
		return !children.isEmpty();
	}

	public boolean hasPolycylinder(PolyCylinder polycylinder) {
		return polycylinders.contains(polycylinder);
	}

	public int countPolyCylinders() {
		return polycylinders.size();
	}

	private void buildBranchGroup() {

		if (propertiesMap.isEmpty()) {
			throw new SeeIT3DException("In order to build the scene graph is necesary to determine the metric calculators that are going to be mapped");
		}

		if (containerBG != null) {
			containerBG.detach();
		}

		Vector3f oldPosition = extractPosition();

		containerBG = new BranchGroup();
		containerBG.setCapability(BranchGroup.ALLOW_DETACH);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		containerBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		containerBG.setUserData(this);

		TransformGroup containerTG = new TransformGroup();
		containerTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		containerTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		containerTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		containerTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		containerTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

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
		buildRelationShipMark(containerTG);

		if (isSelected) {
			activateHighlight();
		}

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

	private void initializePolycylinders() {
		for (PolyCylinder polyCylinder : polycylinders) {
			polyCylinder.initializePolyCylinder(propertiesMap);
		}
	}

	private void calculateDimensions() {
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
	}

	private Vector3f extractPosition() {
		if (containerBG == null) {
			return new Vector3f();
		} else {
			TransformGroup transform = (TransformGroup) containerBG.getChild(0);
			Transform3D transformation = new Transform3D();
			transform.getTransform(transformation);
			Vector3f position = new Vector3f();
			transformation.get(position);
			return position;
		}
	}

	private void buildHighlight(TransformGroup containerTG) {
		float boxWidth = width + ViewConstants.HIGHLIGHT_PADDING;
		float boxHeight = height + ViewConstants.HIGHLIGHT_PADDING;
		float boxDepth = depth + ViewConstants.HIGHLIGHT_PADDING;
		Box surroundingBox = new Box(boxWidth, boxHeight, boxDepth, null);

		Shape3D highlightBox = buildShapeFromBox(surroundingBox);

		TransformGroup highlightBoxTG = new TransformGroup();
		highlightBoxTG.addChild(highlightBox);

		highlighRootNode = new Switch(Switch.CHILD_MASK);
		highlighRootNode.setCapability(Switch.ALLOW_SWITCH_WRITE);
		highlighRootNode.addChild(highlightBoxTG);

		containerTG.addChild(highlighRootNode);
	}

	private Shape3D buildShapeFromBox(Box box) {

		List<Point3f> points = new ArrayList<Point3f>();

		Appearance app = new Appearance();
		app.setColoringAttributes(new ColoringAttributes(preferences.getHighlightColor(), ColoringAttributes.SHADE_FLAT));
		app.setLineAttributes(new LineAttributes(1.2f, LineAttributes.PATTERN_SOLID, false));

		addPointsFromShape(box.getShape(Box.BACK), points);
		addPointsFromShape(box.getShape(Box.BOTTOM), points);
		addPointsFromShape(box.getShape(Box.FRONT), points);
		addPointsFromShape(box.getShape(Box.LEFT), points);
		addPointsFromShape(box.getShape(Box.RIGHT), points);
		addPointsFromShape(box.getShape(Box.TOP), points);

		LineArray boxInLines = new LineArray(24, LineArray.COORDINATES);

		Point3f frontLowerLeft = points.iterator().next();
		for (Point3f point : points) {
			if (point.x <= frontLowerLeft.x && point.y <= frontLowerLeft.y && point.z >= frontLowerLeft.z) {
				frontLowerLeft = point;
			}
		}
		Point3f frontLowerRight = points.iterator().next();
		for (Point3f point : points) {
			if (point.x >= frontLowerRight.x && point.y <= frontLowerRight.y && point.z >= frontLowerRight.z) {
				frontLowerRight = point;
			}
		}

		Point3f frontUpperLeft = points.iterator().next();
		for (Point3f point : points) {
			if (point.x <= frontUpperLeft.x && point.y >= frontUpperLeft.y && point.z >= frontUpperLeft.z) {
				frontUpperLeft = point;
			}
		}

		Point3f frontUpperRight = points.iterator().next();
		for (Point3f point : points) {
			if (point.x >= frontUpperRight.x && point.y >= frontUpperRight.y && point.z >= frontUpperRight.z) {
				frontUpperRight = point;
			}
		}

		Point3f backLowerLeft = points.iterator().next();
		for (Point3f point : points) {
			if (point.x <= backLowerLeft.x && point.y <= backLowerLeft.y && point.z <= backLowerLeft.z) {
				backLowerLeft = point;
			}
		}
		Point3f backLowerRight = points.iterator().next();
		for (Point3f point : points) {
			if (point.x >= backLowerRight.x && point.y <= backLowerRight.y && point.z <= backLowerRight.z) {
				backLowerRight = point;
			}
		}

		Point3f backUpperLeft = points.iterator().next();
		for (Point3f point : points) {
			if (point.x <= backUpperLeft.x && point.y >= backUpperLeft.y && point.z <= backUpperLeft.z) {
				backUpperLeft = point;
			}
		}

		Point3f backUpperRight = points.iterator().next();
		for (Point3f point : points) {
			if (point.x >= backUpperRight.x && point.y >= backUpperRight.y && point.z <= backUpperRight.z) {
				backUpperRight = point;
			}
		}

		// front face
		boxInLines.setCoordinate(0, frontLowerLeft);
		boxInLines.setCoordinate(1, frontUpperLeft);
		boxInLines.setCoordinate(2, frontUpperLeft);
		boxInLines.setCoordinate(3, frontUpperRight);
		boxInLines.setCoordinate(4, frontUpperRight);
		boxInLines.setCoordinate(5, frontLowerRight);
		boxInLines.setCoordinate(6, frontLowerRight);
		boxInLines.setCoordinate(7, frontLowerLeft);

		// back face
		boxInLines.setCoordinate(8, backLowerLeft);
		boxInLines.setCoordinate(9, backUpperLeft);
		boxInLines.setCoordinate(10, backUpperLeft);
		boxInLines.setCoordinate(11, backUpperRight);
		boxInLines.setCoordinate(12, backUpperRight);
		boxInLines.setCoordinate(13, backLowerRight);
		boxInLines.setCoordinate(14, backLowerRight);
		boxInLines.setCoordinate(15, backLowerLeft);

		// sides
		boxInLines.setCoordinate(16, backLowerLeft);
		boxInLines.setCoordinate(17, frontLowerLeft);
		boxInLines.setCoordinate(18, backUpperLeft);
		boxInLines.setCoordinate(19, frontUpperLeft);

		boxInLines.setCoordinate(20, backLowerRight);
		boxInLines.setCoordinate(21, frontLowerRight);
		boxInLines.setCoordinate(22, backUpperRight);
		boxInLines.setCoordinate(23, frontUpperRight);

		Shape3D s = new Shape3D(boxInLines, app);
		return s;
	}

	private void buildRelationShipMark(TransformGroup containerTG) {
		Box surroundingBox = new Box(width / 2, height, depth / 2, null);

		relationMarkNode = new Switch(Switch.CHILD_MASK);
		relationMarkNode.setCapability(Switch.ALLOW_SWITCH_WRITE);

		Appearance app = new Appearance();
		ColoringAttributes color = new ColoringAttributes(preferences.getRelationMarkColor(), ColoringAttributes.SHADE_FLAT);
		app.setColoringAttributes(color);

		TransformGroup tgRelationMarkTop = new TransformGroup();
		Geometry geometryTop = surroundingBox.getShape(Box.TOP).getGeometry();
		Shape3D relationMarkTop = new Shape3D(geometryTop, app);
		tgRelationMarkTop.addChild(relationMarkTop);

		Transform3D transformationTop = new Transform3D();
		tgRelationMarkTop.getTransform(transformationTop);
		transformationTop.setTranslation(new Vector3f(0.0f, -(2 * height + ViewConstants.RELATION_MARK_PADDING), 0.0f));
		tgRelationMarkTop.setTransform(transformationTop);

		TransformGroup tgRelationMarkBottom = new TransformGroup();
		Geometry geometryBottom = surroundingBox.getShape(Box.BOTTOM).getGeometry();
		Shape3D relationMarkBottom = new Shape3D(geometryBottom, app);
		tgRelationMarkBottom.addChild(relationMarkBottom);

		Transform3D transformationBottom = new Transform3D();
		tgRelationMarkBottom.getTransform(transformationBottom);
		transformationBottom.setTranslation(new Vector3f(0.0f, -ViewConstants.RELATION_MARK_PADDING, 0.0f));
		tgRelationMarkBottom.setTransform(transformationBottom);

		TransformGroup mark = new TransformGroup();

		mark.addChild(tgRelationMarkBottom);
		mark.addChild(tgRelationMarkTop);

		relationMarkNode.addChild(mark);

		containerTG.addChild(relationMarkNode);

	}

	public BranchGroup getContainerBG() {
		validateForVisualState();
		return containerBG;
	}

	public void updateVisualRepresentation() {
		buildBranchGroup();
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

	public void updateMapping(BaseMetricCalculator metric, VisualProperty property) {
		propertiesMap.forcePut(metric, property);
		for (PolyCylinder poly : polycylinders) {
			poly.updateMapping(propertiesMap);
		}
	}

	public void removeFromMapping(BaseMetricCalculator metric) {
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

	private void resetDimensions() {
		height = -1;
		width = -1;
		depth = -1;
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

	private void activateHighlight() {
		validateForVisualState();
		changeSwitchNodeState(highlighRootNode, true);
	}

	private void deactiveHighlight() {
		validateForVisualState();
		changeSwitchNodeState(highlighRootNode, false);
	}

	public void activateRelationShipMark() {
		validateForVisualState();
		changeSwitchNodeState(relationMarkNode, true);
	}

	public void deactivateRelationShipMark() {
		validateForVisualState();
		changeSwitchNodeState(relationMarkNode, false);
	}

	private void addPointsFromShape(Shape3D shape, List<Point3f> points) {

		TriangleStripArray geometry = (TriangleStripArray) shape.getGeometry();
		int vertexCount = geometry.getVertexCount();
		for (int i = 0; i < vertexCount; i++) {
			Point3f point = new Point3f();
			geometry.getCoordinate(i, point);
			if (!points.contains(point)) {
				points.add(point);
			}
		}
	}

	private void changeSwitchNodeState(Switch node, boolean newState) {
		BitSet bitSet = new BitSet(node.numChildren());
		for (int i = 0; i < bitSet.size(); i++) {
			bitSet.set(i, newState);
		}
		node.setChildMask(bitSet);
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

	private void validateForVisualState() {
		if (highlighRootNode == null || relationMarkNode == null || containerBG == null) {
			buildBranchGroup();
		}
	}

	public List<BaseMetricCalculator> getMetrics() {
		return Collections.unmodifiableList(metrics);
	}

	public float getWidth() {
		if (width == -1) {
			throw new IllegalStateException("Area not calculated");
		}
		return width;
	}

	public float getDepth() {
		if (depth == -1) {
			throw new IllegalStateException("Area not calculated");
		}
		return depth;
	}

	public BiMap<BaseMetricCalculator, VisualProperty> getPropertiesMap() {
		return propertiesMap;
	}

	public List<Container> getRelatedContainers() {
		return relatedContainers;
	}

	public String getName() {
		return representedObject.getName();
	}

	public String getGranularityLevelName() {
		return representedObject.granularityLevelName(currentLevel);
	}

	public void setSortingProperty(VisualProperty visualProperty) {
		sortingProperty = visualProperty;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public void addPolyCylinder(PolyCylinder polyCylinder) {
		polycylinders.add(polyCylinder);
	}

	public Container createCopy() {

		Container newContainer = new Container(representedObject, metrics);
		newContainer.propertiesMap.clear();
		newContainer.propertiesMap.putAll(propertiesMap);

		for (Container container : relatedContainers) {
			newContainer.addRelatedContainer(container.createCopy());
		}

		for (Container container : children) {
			newContainer.addChildrenContainer(container.createCopy());
		}

		for (PolyCylinder poly : polycylinders) {
			newContainer.addPolyCylinder(poly.createCopy());
		}

		newContainer.containerBG = null;
		return newContainer;
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
		preferences = Preferences.getInstance();
	}
}
