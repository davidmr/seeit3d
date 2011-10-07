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
package seeit3d.internal.utils;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.internal.java.util.LineOfCode;

import com.google.common.base.Function;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Utility class (Visual guides, translations and so on)
 * 
 * @author David Montaño
 * 
 */

public class Utils {

	public static final String ID_PACKAGE_EXPLORER = "org.eclipse.jdt.ui.PackageExplorer";

	public static final String NAVIGATOR_VIEW_ID = "org.eclipse.ui.views.ResourceNavigator";

	private static long containerIdentifier = 0L;

	private static long polyCylinderIdentifier = 0L;

	public static final boolean isDebug = false;

	public static LineOfCode[] buildLinesFromMethod(IMethod method) throws JavaModelException {
		String source = method.getSource();
		String[] linesStr = source.split("\n");
		LineOfCode[] lines = new LineOfCode[linesStr.length];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = new LineOfCode(i, linesStr[i], method);
		}
		return lines;
	}

	public static synchronized long generateContainerIdentifier() {
		containerIdentifier++;
		return containerIdentifier;
	}

	public static synchronized long generatePolyCylinderIdentifier() {
		polyCylinderIdentifier++;
		return polyCylinderIdentifier;
	}

	public static void buildGuide(TransformGroup g) {

		Appearance app = new Appearance();
		ColoringAttributes color = new ColoringAttributes(ViewConstants.RED, ColoringAttributes.SHADE_FLAT);
		app.setColoringAttributes(color);

		Sphere s = new Sphere(0.05f, app);
		g.addChild(s);

	}

	public static void buildAxis(BranchGroup rootObj) {

		float size = 1f;
		BranchGroup axisGroup = new BranchGroup();

		Appearance appX = new Appearance();
		ColoringAttributes caX = new ColoringAttributes(ViewConstants.RED, ColoringAttributes.SHADE_FLAT);
		appX.setColoringAttributes(caX);

		LineArray x = new LineArray(2, LineArray.COORDINATES);
		x.setCoordinates(0, new Point3d[] { new Point3d(size, 0, 0), new Point3d(-size, 0, 0) });
		Shape3D shapeX = new Shape3D(x, appX);

		axisGroup.addChild(shapeX);

		Appearance appY = new Appearance();
		ColoringAttributes caY = new ColoringAttributes(ViewConstants.GREEN, ColoringAttributes.SHADE_FLAT);
		appY.setColoringAttributes(caY);

		LineArray y = new LineArray(2, LineArray.COORDINATES);
		y.setCoordinates(0, new Point3d[] { new Point3d(0, size, 0), new Point3d(0, -size, 0) });
		Shape3D shapeY = new Shape3D(y, appY);

		axisGroup.addChild(shapeY);

		Appearance appZ = new Appearance();
		ColoringAttributes caZ = new ColoringAttributes(ViewConstants.BLUE, ColoringAttributes.SHADE_FLAT);
		appZ.setColoringAttributes(caZ);

		LineArray z = new LineArray(2, LineArray.COORDINATES);
		z.setCoordinates(0, new Point3d[] { new Point3d(0, 0, size), new Point3d(0, 0, -size) });
		Shape3D shapeZ = new Shape3D(z, appZ);

		axisGroup.addChild(shapeZ);

		rootObj.addChild(axisGroup);
	}

	public static void translateTranformGroup(TransformGroup transformGroup, Vector3f newPosition) {
		Transform3D translation = new Transform3D();
		transformGroup.getTransform(translation);
		translation.setTranslation(newPosition);
		transformGroup.setTransform(translation);

	}

	public static Shape3D buildShapeFromBox(Box box, Color3f colorBox) {

		List<Point3f> points = new ArrayList<Point3f>();

		Appearance app = new Appearance();
		app.setColoringAttributes(new ColoringAttributes(colorBox, ColoringAttributes.SHADE_FLAT));
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

	public static void addPointsFromShape(Shape3D shape, List<Point3f> points) {

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

	public static <T> void applyFunction(Iterable<T> collection, Function<T, T> function) {
		for (T object : collection) {
			function.apply(object);
		}
	}

}
