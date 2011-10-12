package seeit3d.internal.base.ui.ide.view;


import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

public class ResizeListener implements Listener {

	private final Control control;

	private final boolean sashOnTop;

	public ResizeListener(Control control, boolean sashOnTop) {
		this.control = control;
		this.sashOnTop = sashOnTop;
	}

	@Override
	public void handleEvent(Event event) {
		Sash sash = (Sash) event.widget;
		Point sashSize = sash.getSize();
		Composite parent = control.getParent();
		int height = 0;
		if (sashOnTop) {
			Point parentSize = parent.getSize();
			height = parentSize.y - event.y - 2 * sashSize.y;
		} else {
			height = event.y - sashSize.y;
		}
		GridData newData = new GridData(GridData.FILL_HORIZONTAL);
		newData.heightHint = height;
		control.setLayoutData(newData);
		parent.layout(true, true);
	}

}
