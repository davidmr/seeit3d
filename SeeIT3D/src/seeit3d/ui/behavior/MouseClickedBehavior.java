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
package seeit3d.ui.behavior;

import static seeit3d.general.bus.EventBus.publishEvent;

import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import seeit3d.general.bus.events.ChangeSelectionEvent;
import seeit3d.general.bus.events.OpenEditorEvent;
import seeit3d.general.model.Container;
import seeit3d.general.model.PolyCylinder;

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

		Container selectedContainer = null;
		PolyCylinder selectedPolyCylinder = null;

		pickCanvas.setShapeLocation(xPos, yPos);
		pickResult = pickCanvas.pickAll();
		selectedContainer = PickUtils.findContainerAssociated(pickResult);
		selectedPolyCylinder = PickUtils.findPolyCylinderAssociated(pickResult);
		if (doubleClick()) {
			if (selectedPolyCylinder != null) {
				publishEvent(new OpenEditorEvent(selectedPolyCylinder));
			}
		} else {
			boolean toggleContainerSelection = mevent.isControlDown();
			boolean togglePolycylinderSelection = mevent.isShiftDown();
			publishEvent(new ChangeSelectionEvent(selectedContainer, selectedPolyCylinder, toggleContainerSelection, togglePolycylinderSelection));
		}

		lastPressedTime = System.currentTimeMillis();
	}

	private boolean doubleClick() {
		long currentTime = System.currentTimeMillis();
		return currentTime - lastPressedTime < DELTA_PRESS_MBUTTON;
	}

}
