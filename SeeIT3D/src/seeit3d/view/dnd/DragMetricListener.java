package seeit3d.view.dnd;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Label;

import seeit3d.metrics.BaseMetricCalculator;

public class DragMetricListener implements DragSourceListener {

	private final Label label;

	public DragMetricListener(Label label) {
		super();
		this.label = label;
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE) {
			label.setVisible(false);
			label.dispose();
		}
	}

	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TransferMetric.getInstance().isSupportedType(event.dataType)) {
			BaseMetricCalculator metric = (BaseMetricCalculator) label.getData();
			event.data = metric;
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {

	}

}
