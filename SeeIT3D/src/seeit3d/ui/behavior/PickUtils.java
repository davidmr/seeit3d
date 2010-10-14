package seeit3d.ui.behavior;

import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.TransformGroup;

import seeit3d.general.model.Container;
import seeit3d.general.model.PolyCylinder;

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
			TransformGroup transformGroup = PickUtils.chooseContainerMainTransformGroup(pickResults);
			if (transformGroup != null) {
				return (Container) transformGroup.getUserData();
			}
		}
		return null;
	}

	public static PolyCylinder findPolyCylinderAssociated(PickResult[] pickResults) {
		if (pickResults != null) {
			for (PickResult pickResult : pickResults) {
				SceneGraphPath sceneGraphPath = pickResult.getSceneGraphPath();
				for (int i = 0; i < sceneGraphPath.nodeCount(); i++) {
					Node node = sceneGraphPath.getNode(i);
					if (node instanceof Box) {
						return (PolyCylinder) node.getUserData();
					}
				}
			}
		}
		return null;
	}
}
