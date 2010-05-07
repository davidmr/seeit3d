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
package seeit3d.behavior;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.*;
import javax.vecmath.Matrix3d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * Class to provide basic functionality for changing transformations on selected objects in the scene. This class is based on the PickMouseBehavior of Java 3D
 * 
 * @author David Montaño
 */
public abstract class MouseOperationBehavior extends MouseBehavior {

	private static final int MIN_VALUE_TO_TRIGGER = 50;

	private final ViewingPlatform viewingPlatform;

	private final int mouseButtonToWakeUp;

	private int pressedButton = MouseEvent.NOBUTTON;

	private MouseBehaviorCallback callback;

	public MouseOperationBehavior(int format, ViewingPlatform viewingPlatform) {
		super(format);
		this.viewingPlatform = viewingPlatform;
		mouseButtonToWakeUp = getMouseButtonToWakeUp();
	}

	public abstract int getMouseButtonToWakeUp();

	protected abstract int operationToNotifyType();

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(Enumeration criteria) {
		while (criteria.hasMoreElements()) {
			WakeupCriterion wakeup = (WakeupCriterion) criteria.nextElement();
			if (wakeup instanceof WakeupOnAWTEvent) {
				AWTEvent[] events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
				if (events.length > 0) {
					MouseEvent evt = (MouseEvent) events[events.length - 1];
					doProcess(evt);
				}
			}
		}
		wakeupOn(mouseCriterion);
	}

	@Override
	public void processMouseEvent(MouseEvent evt) {
		super.processMouseEvent(evt);
		if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
			pressedButton = evt.getButton();
			return;
		}
		if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
			pressedButton = MouseEvent.NOBUTTON;
			return;
		}
	}

	private void doProcess(MouseEvent evt) {
		processMouseEvent(evt);
		if (wakeUp && (flags & MANUAL_WAKEUP) != 0) {
			int x = evt.getX();
			int y = evt.getY();
			if (isButtonPressedCorrectly(evt) && positionChanged(x, y) && isSmoothMovement(x, y)) {
				float dx = x - x_last;
				float dy = y - y_last;

				Transform3D transform = new Transform3D();
				transformGroup.getTransform(transform);

				Transform3D newTransform = buildTransformation(transform, dx, dy);

				transformGroup.setTransform(newTransform);

				notifyCallback(newTransform);
			}
			x_last = x;
			y_last = y;
		}
	}

	private void notifyCallback(Transform3D transform) {
		if (callback != null) {
			int type = operationToNotifyType();
			callback.transformChanged(type, transform);
		}
	}

	protected Matrix3d getRotationFromViewersPosition() {
		TransformGroup viewPlatformTransform = viewingPlatform.getViewPlatformTransform();
		Transform3D viewersTranform = new Transform3D();
		viewPlatformTransform.getTransform(viewersTranform);
		Matrix3d viewRotMatrix = new Matrix3d();
		viewersTranform.get(viewRotMatrix);
		return viewRotMatrix;
	}

	public abstract Transform3D buildTransformation(Transform3D transform, float dx, float dy);

	private boolean isSmoothMovement(int x, int y) {
		return Math.abs(x - x_last) < MIN_VALUE_TO_TRIGGER && Math.abs(y - y_last) < MIN_VALUE_TO_TRIGGER;
	}

	private boolean positionChanged(int x, int y) {
		return x != x_last || y != y_last;
	}

	private boolean isButtonPressedCorrectly(MouseEvent evt) {
		return evt.getID() == MouseEvent.MOUSE_DRAGGED && buttonPress && pressedButton == mouseButtonToWakeUp;
	}

	public void setupCallback(MouseBehaviorCallback callback) {
		this.callback = callback;
	}

}
