package seeit3d.visual.relationships.imp;

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.vecmath.Point3f;

import seeit3d.general.model.Container;

public class LineBaseGenerator extends GeometryBasedGenerator {

	private static final String NAME = "Lines";

	@Override
	public Geometry createGeometryBetweenContainers(Container origin, Container destination) {
		Point3f originPoint = extractPointFromVector(origin.getPosition());
		Point3f destinationPoint = extractPointFromVector(destination.getPosition());

		LineArray line = new LineArray(2, LineArray.COORDINATES);
		line.setCoordinate(0, originPoint);
		line.setCoordinate(1, destinationPoint);
		return line;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
