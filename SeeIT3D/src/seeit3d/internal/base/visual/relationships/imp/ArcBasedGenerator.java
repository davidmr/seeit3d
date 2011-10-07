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
package seeit3d.internal.base.visual.relationships.imp;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import javax.media.j3d.Geometry;
import javax.media.j3d.LineArray;
import javax.media.j3d.Transform3D;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import seeit3d.internal.base.model.Container;

/**
 * Relationship generator based on arcs between related elements
 * 
 * @author David Montaño
 * 
 */
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

		Vector3f diff = new Vector3f();
		diff.sub(originVector, destinationVector);

		Vector3f geometryVector = new Vector3f(1.0f, 0.0f, 0.0f);

		Vector3f perpendicularToDiff = new Vector3f();
		perpendicularToDiff.cross(geometryVector, diff);

		float angle = geometryVector.angle(diff);

		Matrix4f rotation = new Matrix4f();
		AxisAngle4f axisAngle = new AxisAngle4f(perpendicularToDiff, angle);
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
