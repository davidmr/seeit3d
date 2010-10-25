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
package seeit3d.base.ui.ide.view.dnd;

import static seeit3d.base.bus.EventBus.*;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Group;

import seeit3d.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.base.ui.actions.RemoveMetricFromContainerFunction;

/**
 * This class listens for metrics drop on the metrics group and removing them from the visualization
 * 
 * @author David Montaño
 * 
 */
public class DropMetricOnMetricContainerListener implements DropTargetListener {

	private final Group groupToAdd;

	public DropMetricOnMetricContainerListener(Group groupToAdd) {
		this.groupToAdd = groupToAdd;
	}

	@Override
	public void dropAccept(DropTargetEvent event) {}

	@Override
	public void drop(DropTargetEvent event) {
		if (TransferMetric.getInstance().isSupportedType(event.currentDataType)) {
			MetricCalculator metric = (MetricCalculator) event.data;
			publishEvent(createEvent(metric));
			DragAndDropHelper.createMetricDraggableLabel(groupToAdd, metric);
			groupToAdd.layout();
		}
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {}

	@Override
	public void dragLeave(DropTargetEvent event) {}

	@Override
	public void dragEnter(DropTargetEvent event) {}

	public PerformOperationOnSelectedContainersEvent createEvent(final MetricCalculator metric){
		FunctionToApplyOnContainer function = new RemoveMetricFromContainerFunction(metric);
		return new PerformOperationOnSelectedContainersEvent(function, true);
	}
	
}
