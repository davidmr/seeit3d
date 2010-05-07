package seeit3d.relationships.imp;

import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.model.representation.Container;
import seeit3d.relationships.RelationShipVisualGenerator;

import com.sun.j3d.utils.pickfast.behaviors.PickingCallback;

public class LineBaseGenerator implements RelationShipVisualGenerator, PickingCallback {

	private static final String NAME = "Lines";

	private final SeeIT3DManager manager;

	public LineBaseGenerator() {
		this.manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void transformChanged(int type, TransformGroup tg) {
		// TODO find out how to know which node has to be moved
	}

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {
		List<Container> relatedContainers = baseContainer.getRelatedContainers();

		for (Container related : relatedContainers) {
			addLineBetweenContainer(baseContainer, related);
		}

		this.manager.setupTranslationCallback(this);

		return relatedContainers;
	}

	private void addLineBetweenContainer(Container origin, Container destination) {
		LineArray line = new LineArray(2, LineArray.COORDINATES);
		Point3f originPoint = extractPointFromVector(origin.getPosition());
		Point3f destinationPoint = extractPointFromVector(destination.getPosition());
		line.setCoordinate(0, originPoint);
		line.setCoordinate(1, destinationPoint);

		Appearance appearance = new Appearance();
		LineAttributes lineAttributes = new LineAttributes(10.0f, LineAttributes.PATTERN_SOLID, true);
		appearance.setLineAttributes(lineAttributes);

		ColoringAttributes coloringAttributes = new ColoringAttributes(manager.getRelationMarkColor(), ColoringAttributes.SHADE_FLAT);
		appearance.setColoringAttributes(coloringAttributes);

		Shape3D relationMark = new Shape3D(line, appearance);
		origin.getContainerBG().addChild(relationMark);
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
