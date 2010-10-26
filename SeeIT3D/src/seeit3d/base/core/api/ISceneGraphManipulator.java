/**
 * Copyright (C) 2010  David Montaño
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package seeit3d.base.core.api;

import seeit3d.base.core.handler.SeeIT3DCanvas;
import seeit3d.base.model.Container;

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
