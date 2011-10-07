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
package seeit3d.internal.base.ui.behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Node;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.TransformGroup;

import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.picking.PickResult;

/**
 * Utility class for picking operations
 * 
 * @author David Montaño
 * 
 */
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
