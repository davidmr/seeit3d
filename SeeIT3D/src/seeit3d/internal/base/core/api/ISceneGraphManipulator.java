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
package seeit3d.internal.base.core.api;

import seeit3d.internal.base.core.handler.SeeIT3DCanvas;
import seeit3d.internal.base.model.Container;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

/**
 * Interface that defines the actions of the scene graph manipulator
 * 
 * @author David Montaño
 * 
 */
public interface ISceneGraphManipulator {

	void initialize();

	void setupTranslationCallback(PickingCallback callback);

	void unregisterPickingCallback(PickingCallback callback);

	void activateSelectionTool();

	void changeOrbitState(boolean enabled);

	void setViewersPosition(float maxX);

	void removeScene(Container container);

	void clearScene();

	void rebuildSceneGraph();

	SeeIT3DCanvas getCanvas();

}
