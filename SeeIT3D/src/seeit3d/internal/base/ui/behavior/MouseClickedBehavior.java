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

import static com.google.common.collect.Lists.newArrayList;
import static seeit3d.internal.base.bus.EventBus.publishEvent;

import java.util.List;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import seeit3d.internal.base.bus.events.ChangeSelectionEvent;
import seeit3d.internal.base.bus.events.OpenEditorEvent;
import seeit3d.internal.base.model.Container;
import seeit3d.internal.base.model.PolyCylinder;

import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

/**
 * This class is called when an object in the scene is selected, and handle the input correctly to open or not the text editor.
 * 
 * @author David Montaño
 * 
 */
public class MouseClickedBehavior extends PickMouseBehavior {

	private static final int DELTA_PRESS_MBUTTON = 250;

	private long lastPressedTime = 0;

	public MouseClickedBehavior(Canvas3D canvas, BranchGroup rootBg, Bounds bounds) {
		super(canvas, rootBg, bounds);
		setSchedulingBounds(bounds);
		pickCanvas.setMode(PickTool.GEOMETRY);
	}

	@Override
	public void updateScene(int xPos, int yPos) {

		PickResult[] pickResult = null;

		pickCanvas.setShapeLocation(xPos, yPos);
		pickResult = pickCanvas.pickAllSorted();
		Container selectedContainer = PickUtils.findContainerAssociated(pickResult);
		List<PolyCylinder> selectedPolycylinder = PickUtils.findPolyCylinderAssociated(pickResult);
		if (doubleClick()) {
			if (!selectedPolycylinder.isEmpty()) {
				PolyCylinder firstPoly = selectedPolycylinder.iterator().next();
				publishEvent(new OpenEditorEvent(firstPoly));
			}
		} else {
			boolean toggleContainerSelection = mevent.isControlDown();
			boolean togglePolycylinderSelection = mevent.isShiftDown();
			List<Container> containers = newArrayList(selectedContainer);
			publishEvent(new ChangeSelectionEvent(containers, selectedPolycylinder, toggleContainerSelection, togglePolycylinderSelection));
		}

		lastPressedTime = System.currentTimeMillis();
	}

	private boolean doubleClick() {
		long currentTime = System.currentTimeMillis();
		return currentTime - lastPressedTime < DELTA_PRESS_MBUTTON;
	}

}
