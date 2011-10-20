package seeit3d.internal.base.ui.ide.view.dnd;

import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractDropMetricListener implements DropTargetListener {

	private static final int OFFSET = -10;

	private Color sourceColor;

	@Override
	public void dragLeave(DropTargetEvent event) {
		DropTarget target = (DropTarget) event.widget;
		Group group = (Group) target.getControl();
		if (sourceColor != null) {
			group.setBackground(sourceColor);
		}
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		DropTarget target = (DropTarget) event.widget;
		Group group = (Group) target.getControl();
		sourceColor = group.getBackground();
		Color newColor = new Color(event.display, sourceColor.getRed() + OFFSET, sourceColor.getGreen() + OFFSET, sourceColor.getBlue() + OFFSET);
		group.setBackground(newColor);
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {}

	@Override
	public void dragOver(DropTargetEvent event) {}

	@Override
	public void dropAccept(DropTargetEvent event) {}

}
