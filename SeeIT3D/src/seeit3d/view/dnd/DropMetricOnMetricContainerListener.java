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
package seeit3d.view.dnd;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Group;

import seeit3d.manager.SeeIT3DManager;
import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.utils.DragAndDropHelper;

/**
 * This class listens for metrics drop on the metrics group and removing them from the visualization
 * 
 * @author David Montaño
 * 
 */
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
