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
package seeit3d.core.handler;

import static seeit3d.general.bus.EventBus.publishEvent;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

import seeit3d.general.bus.events.SelectionToolEndedEvent;
import seeit3d.utils.ViewConstants;

import com.sun.j3d.utils.picking.PickCanvas;

/**
 * Canvas based on Canvas3D from Java3D. It allows to modify certain parts of the visualization algorithm like the rendering hints
 * 
 * @author David Montaño
 * 
 */
public class SeeIT3DCanvas extends Canvas3D {

	private static final long serialVersionUID = 1L;
	
	private final J3DGraphics2D render2D;
	
	private final BranchGroup rootBG;

	private boolean isSelectionToolActivated = false;

	private boolean isMousePressed = false;

	private int xButtonPressed = 0;

	private int yButtonPressed = 0;

	private int xCurrent = 0;

	private int yCurrent = 0;

	

	SeeIT3DCanvas(GraphicsConfiguration configuration, BranchGroup rootBG) {
		super(configuration);
		this.rootBG = rootBG;
		render2D = getGraphics2D();
		render2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	@Override
	protected void processMouseEvent(MouseEvent e) {
		super.processMouseEvent(e);
		if (isSelectionToolActivated) {
			if (e.getID() == MouseEvent.MOUSE_PRESSED) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					xButtonPressed = e.getX();
					yButtonPressed = e.getY();
					isMousePressed = true;
				}
			}

			if (e.getID() == MouseEvent.MOUSE_RELEASED) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					isMousePressed = false;
					selectionEnded();
				}
			}
		}
	}

	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		super.processMouseMotionEvent(e);
		xCurrent = e.getX();
		yCurrent = e.getY();
	}

	@Override
	public void postRender() {
		super.postRender();
		if (isSelectionToolActivated && isMousePressed) {
			Rectangle selection = buildSelectionRectangle();
			render2D.setColor(Color.black);
			render2D.drawRect(selection.x, selection.y, selection.width, selection.height);						
			render2D.setStroke(ViewConstants.SELECTION_STROKE);
			render2D.flush(true);
		}
	}

	private Rectangle buildSelectionRectangle() {
		int x = xButtonPressed;
		int y = yButtonPressed;

		int width = xCurrent - xButtonPressed;
		int height = yCurrent - yButtonPressed;

		if (width < 0) {
			x = xCurrent;
		}

		if (height < 0) {
			y = yCurrent;
		}

		width = Math.abs(width);
		height = Math.abs(height);

		return new Rectangle(x, y, width, height);
	}

	public void activateSelectionTool() {
		isSelectionToolActivated = true;
	}

	public void deactivateSelectionTool() {
		isSelectionToolActivated = false;
	}

	private void selectionEnded() {
		PickCanvas pickCanvas = new PickCanvas(this, rootBG);
		Rectangle area = buildSelectionRectangle();		
		publishEvent(new SelectionToolEndedEvent(area, pickCanvas));		
		xButtonPressed = 0;
		yButtonPressed = 0;
		deactivateSelectionTool();
	}

}
