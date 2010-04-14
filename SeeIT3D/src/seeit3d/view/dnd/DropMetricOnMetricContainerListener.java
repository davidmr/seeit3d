package seeit3d.view.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Group;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.utils.DragAndDropHelper;

public class DropMetricOnMetricContainerListener implements DropTargetListener {

	private final Group groupToAdd;

	private final SeeIT3DManager manager;

	public DropMetricOnMetricContainerListener(Group groupToAdd) {
		this.groupToAdd = groupToAdd;
		manager = SeeIT3DManager.getInstance();
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
	}

	@Override
	public void drop(DropTargetEvent event) {
		if (TransferMetric.getInstance().isSupportedType(event.currentDataType)) {
			BaseMetricCalculator metric = (BaseMetricCalculator) event.data;
			manager.removeSelectContainersMapping(metric);
			DragAndDropHelper.createMetricDraggableLabel(groupToAdd, metric);
			groupToAdd.layout();
		}
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
	}

}
