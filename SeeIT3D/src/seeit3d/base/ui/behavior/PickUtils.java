package seeit3d.base.ui.behavior;

import java.util.*;

import javax.media.j3d.*;

import seeit3d.base.model.Container;
import seeit3d.base.model.PolyCylinder;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.PickResult;

public class PickUtils {

	public static TransformGroup chooseContainerMainTransformGroup(PickResult[] pickResults) {
		if (pickResults != null) {
			for (PickResult pickResult : pickResults) {
				TransformGroup transformGroup = (TransformGroup) pickResult.getNode(PickResult.TRANSFORM_GROUP);
				if (transformGroup != null) {
					Object data = transformGroup.getUserData();
					if (data instanceof Container) {
						if (transformGroup.getCapability(TransformGroup.ALLOW_TRANSFORM_READ) && transformGroup.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE)) {
							return transformGroup;
						}
					}
				}
			}
		}
		return null;
	}

	public static Container findContainerAssociated(PickResult[] pickResults) {
		if (pickResults != null) {
			TransformGroup transformGroup = chooseContainerMainTransformGroup(pickResults);
			if (transformGroup != null) {
				return (Container) transformGroup.getUserData();
			}
		}
		return null;
	}

	public static List<PolyCylinder> findPolyCylinderAssociated(PickResult[] pickResults) {
		List<PolyCylinder> polycylinders = new ArrayList<PolyCylinder>();
		if (pickResults != null) {
			List<PickResult> list = Arrays.asList(pickResults);
			Collections.reverse(list);
			pickResults = list.toArray(new PickResult[list.size()]);
			for (PickResult pickResult : pickResults) {
				SceneGraphPath sceneGraphPath = pickResult.getSceneGraphPath();
				for (int i = 0; i < sceneGraphPath.nodeCount(); i++) {
					Node node = sceneGraphPath.getNode(i);
					if (node instanceof Box) {
						Object userData = node.getUserData();
						if (userData != null && userData instanceof PolyCylinder) {
							PolyCylinder polyFound = (PolyCylinder) userData;
							if (!polycylinders.contains(polyFound)) {
								polycylinders.add(polyFound);
							}
						}
					}
				}
			}
		}
		return polycylinders;
	}
}
