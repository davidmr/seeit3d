package seeit3d.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.view.dnd.DragMetricListener;
import seeit3d.view.dnd.TransferMetric;

public class DragAndDropHelper {

	public static Label createMetricDraggableLabel(Composite parent, BaseMetricCalculator metric) {
		Label label = new Label(parent, SWT.CENTER);
		GridData labelData = new GridData(GridData.CENTER | GridData.FILL_HORIZONTAL);
		label.setLayoutData(labelData);
		DragSource dragSource = new DragSource(label, DND.DROP_MOVE);
		dragSource.setTransfer(getAcceptableTransfers());
		dragSource.addDragListener(new DragMetricListener(label));
		label.setText(metric.getMetricName());
		label.setData(metric);
		return label;
	}

	public static void registerAsDroppable(Group group, DropTargetListener listener) {
		DropTarget dropTarget = new DropTarget(group, DND.DROP_MOVE);
		dropTarget.setTransfer(getAcceptableTransfers());
		dropTarget.addDropListener(listener);
	}

	private static Transfer[] getAcceptableTransfers() {
		return new Transfer[] { TransferMetric.getInstance() };
	}
}
