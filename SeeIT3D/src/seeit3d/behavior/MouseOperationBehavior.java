package seeit3d.behavior;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.*;
import javax.vecmath.Matrix3d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.universe.ViewingPlatform;

public abstract class MouseOperationBehavior extends MouseBehavior {

	private static final int MIN_VALUE_TO_TRIGGER = 50;

	private final ViewingPlatform viewingPlatform;

	private int pressedButton = MouseEvent.NOBUTTON;

	private final int mouseButtonToWakeUp;

	public MouseOperationBehavior(int format, ViewingPlatform viewingPlatform) {
		super(format);
		this.viewingPlatform = viewingPlatform;
		mouseButtonToWakeUp = getMouseButtonToWakeUp();
	}

	public abstract int getMouseButtonToWakeUp();

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

				Transform3D newTranform = buildTransformation(transform, dx, dy);

				transformGroup.setTransform(newTranform);
			}
			x_last = x;
			y_last = y;
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

}
