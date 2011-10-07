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

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3f;

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

	protected abstract int operationToNotifyOnCallbackType();

	@Override
	public void processStimulus(@SuppressWarnings("rawtypes") Enumeration criteria) {
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
			int type = operationToNotifyOnCallbackType();
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

	protected Vector3f getViewersPosition() {
		TransformGroup viewPlatformTransform = viewingPlatform.getViewPlatformTransform();
		Transform3D viewersTranform = new Transform3D();
		viewPlatformTransform.getTransform(viewersTranform);
		Vector3f position = new Vector3f();
		viewersTranform.get(position);
		return position;
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
