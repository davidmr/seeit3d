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
package seeit3d.internal.base.ui.ide.view.dnd;

import static seeit3d.internal.base.bus.EventBus.publishEvent;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Group;

import seeit3d.analysis.metric.MetricCalculator;
import seeit3d.internal.base.bus.events.PerformOperationOnSelectedContainersEvent;
import seeit3d.internal.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.internal.base.model.VisualProperty;
import seeit3d.internal.base.ui.actions.UpdateMappingFunction;
import seeit3d.internal.base.ui.ide.view.MappingViewComposite;

/**
 * This class listens for metrics dropped on a visual property group, indicating that the mapping needs to be changed
 * 
 * @author David Montaño
 * 
 */
public class DropMetricOnVisualPropertyListener extends AbstractDropMetricListener {

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

	public PerformOperationOnSelectedContainersEvent createEvent(final MetricCalculator metric, final VisualProperty visualProp) {
		FunctionToApplyOnContainer function = new UpdateMappingFunction(visualProp, metric);
		return new PerformOperationOnSelectedContainersEvent(function, true);
	}
}
