package seeit3d.visual.relationships.imp;

import static java.lang.Math.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import seeit3d.general.model.Container;

public class ArcBasedGenerator extends GeometryBasedGenerator {

	private static final String NAME = "Arcs";

	@Override
	public Geometry createGeometryBetweenContainers(Container origin, Container destination) {

		Point3f originPoint = extractPointFromVector(origin.getPosition());
		Point3f destinationPoint = extractPointFromVector(destination.getPosition());

		float distance = originPoint.distance(destinationPoint);

		int segments = 20;
		float portionInDegrees = 90f;


		LineArray line = new LineArray(segments * 2, LineArray.COORDINATES);

		float angle = portionInDegrees / segments;
		double radius = (distance / 2) / sin(toRadians(portionInDegrees / 2));

		float yCorrection = (float) (radius / cos(toRadians(portionInDegrees / 2)));

		int coordinateNumber = 0;
		for (float i = 0; i < segments; i++) {

			float evaluatedAngle = (90 + portionInDegrees / 2) - (angle * i);
			float x = (float) (cos(toRadians(evaluatedAngle)) * radius);
			float y = (float) (sin(toRadians(evaluatedAngle)) * radius);
			y -= yCorrection / 2;

			Point3f firstPoint = new Point3f(x, y, 0.0f);

			evaluatedAngle = (90 + portionInDegrees / 2) - (angle * (i + 1));
			x = (float) (cos(toRadians(evaluatedAngle)) * radius);
			y = (float) (sin(toRadians(evaluatedAngle)) * radius);
			y -= yCorrection / 2;

			Point3f secondPoint = new Point3f(x, y, 0.0f);

			line.setCoordinate(coordinateNumber, firstPoint);
			coordinateNumber++;
			line.setCoordinate(coordinateNumber, secondPoint);
			coordinateNumber++;
		}

		return line;

	}

	@Override
	public Transform3D createTransformation(Container origin, Container destination) {

		Vector3f originVector = origin.getPosition();
		Vector3f destinationVector = destination.getPosition();

		Vector3f perpendicular = new Vector3f();
		perpendicular.cross(originVector, destinationVector);

		float angle = destinationVector.angle(originVector);

		Matrix4f rotation = new Matrix4f();
		AxisAngle4d axisAngle = new AxisAngle4d(perpendicular.x, perpendicular.y, perpendicular.z, angle);
		rotation.set(axisAngle);

		Vector3f midPoint = new Vector3f(originVector);
		midPoint.interpolate(destinationVector, 0.5f);
		Matrix4f traslationMatrix = new Matrix4f();
		traslationMatrix.set(midPoint);

		Matrix4f resultMatrix = new Matrix4f(traslationMatrix);
		resultMatrix.mul(rotation);

		Transform3D result = new Transform3D(resultMatrix);

		return result;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
