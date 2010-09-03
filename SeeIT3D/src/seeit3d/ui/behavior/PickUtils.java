package seeit3d.ui.behavior;

import javax.media.j3d.TransformGroup;

import seeit3d.general.model.Container;

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
}
