package seeit3d.utils;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.model.ContainerRepresentedObject;
import seeit3d.model.representation.Container;
import seeit3d.model.representation.LineOfCode;

import com.sun.j3d.utils.geometry.Sphere;

/**
 * Utility class (Default mappings, Visual guides)
 * 
 * @author David
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

	public static Container emptyContainer() {
		ContainerRepresentedObject representation = new ContainerRepresentedObject() {

			private static final long serialVersionUID = -4917184452995076729L;

			@Override
			public String getName() {
				return "";
			}

			@Override
			public String granularityLevelName(int currentLevel) {
				return "";
			}
		};

		List<BaseMetricCalculator> metrics = new ArrayList<BaseMetricCalculator>();
		return new Container(representation, metrics);
	}

}
