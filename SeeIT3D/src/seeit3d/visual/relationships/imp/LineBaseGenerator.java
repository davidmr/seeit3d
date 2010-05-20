package seeit3d.visual.relationships.imp;

import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.core.handler.SeeIT3DManager;
import seeit3d.core.handler.utils.IContainersLayoutListener;
import seeit3d.core.model.Container;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

import com.sun.j3d.utils.pickfast.behaviors.PickingCallback;

public class LineBaseGenerator implements ISceneGraphRelationshipGenerator, PickingCallback, IContainersLayoutListener {

	private static final String NAME = "Lines";

	private final SeeIT3DManager manager;

	private final Map<Container, Shape3D> relatedShapes;

	private Container baseContainer;

	public LineBaseGenerator() {
		this.manager = SeeIT3DManager.getInstance();
		this.manager.registerContainersLayoutListener(this);
		relatedShapes = new TreeMap<Container, Shape3D>();
	}

	@Override
	public void transformChanged(int type, TransformGroup tg) {
		Container movedContainer = (Container) tg.getParent().getUserData();

		if (movedContainer.equals(baseContainer)) {
			for (Map.Entry<Container, Shape3D> containerShape : relatedShapes.entrySet()) {
				LineArray line = createLineContainers(baseContainer, containerShape.getKey());
				containerShape.getValue().setGeometry(line);
			}
		} else {
			LineArray line = createLineContainers(baseContainer, movedContainer);
			Shape3D relationMark = relatedShapes.get(movedContainer);
			if (relationMark != null) {
				relationMark.setGeometry(line);
			}
		}
	}

	@Override
	public void containerLayoutChanged() {
		for (Map.Entry<Container, Shape3D> containerShape : relatedShapes.entrySet()) {
			LineArray line = createLineContainers(baseContainer, containerShape.getKey());
			containerShape.getValue().setGeometry(line);
		}
	}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {
		List<Container> relatedContainers = baseContainer.getRelatedContainers();

		for (Container related : relatedContainers) {
			addLineBetweenContainer(baseContainer, related);
		}

		this.baseContainer = baseContainer;

		return relatedContainers;
	}

	private void addLineBetweenContainer(Container baseContainer, Container relatedContainer) {
		LineArray line = createLineContainers(baseContainer, relatedContainer);

		Appearance appearance = new Appearance();
		LineAttributes lineAttributes = new LineAttributes(2.0f, LineAttributes.PATTERN_SOLID, true);
		appearance.setLineAttributes(lineAttributes);

		ColoringAttributes coloringAttributes = new ColoringAttributes(manager.getRelationMarkColor(), ColoringAttributes.NICEST);
		appearance.setColoringAttributes(coloringAttributes);

		Shape3D relationMark = new Shape3D(line, appearance);
		relationMark.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		relationMark.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		baseContainer.getContainerBG().addChild(relationMark);

		relatedShapes.put(relatedContainer, relationMark);
	}

	private LineArray createLineContainers(Container origin, Container destination) {

		Point3f originPoint = extractPointFromVector(origin.getPosition());
		Point3f destinationPoint = extractPointFromVector(destination.getPosition());

		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinate(0, originPoint);
		line.setCoordinate(1, destinationPoint);
		return line;
	}

	private Point3f extractPointFromVector(Vector3f vector) {
		float[] coordinates = new float[3];
		vector.get(coordinates);
		return new Point3f(coordinates);
	}

	@Override
	public String getName() {
		return NAME;
	}


}
