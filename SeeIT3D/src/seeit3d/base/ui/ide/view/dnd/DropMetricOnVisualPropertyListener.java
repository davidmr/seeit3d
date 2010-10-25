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
import seeit3d.base.model.VisualProperty;
import seeit3d.base.model.generator.metrics.MetricCalculator;
import seeit3d.base.ui.actions.UpdateMappingFunction;
import seeit3d.base.ui.ide.view.MappingViewComposite;

/**
 * This class listens for metrics dropped on a visual property group, indicating that the mapping needs to be changed
 * 
 * @author David Montaño
 * 
 */
public class DropMetricOnVisualPropertyListener implements DropTargetListener {

	private final Group groupToAdd;

	public DropMetricOnVisualPropertyListener(Group groupToAdd) {
		this.groupToAdd = groupToAdd;
	}

	@Override
	public void dropAccept(DropTargetEvent event) {}

	@Override
	public void drop(DropTargetEvent event) {
		if (TransferMetric.getInstance().isSupportedType(event.currentDataType)) {
			MetricCalculator metric = (MetricCalculator) event.data;
			VisualProperty visualProperty = (VisualProperty) groupToAdd.getData(MappingViewComposite.VISUAL_PROPERTY);
			publishEvent(createEvent(metric, visualProperty));
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

	public PerformOperationOnSelectedContainersEvent createEvent(final MetricCalculator metric, final VisualProperty visualProp) {
		FunctionToApplyOnContainer function = new UpdateMappingFunction(visualProp, metric);
		return new PerformOperationOnSelectedContainersEvent(function, true);
	}
}
