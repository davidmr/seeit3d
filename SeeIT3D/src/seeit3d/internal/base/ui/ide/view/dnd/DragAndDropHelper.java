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

import seeit3d.analysis.metric.MetricCalculator;

/**
 * This class provide utility methods to create register and handle different options of Drag and Drop operations for mapping view
 * 
 * @author David Montaño
 * 
 */
public class DragAndDropHelper {

	public static Label createMetricDraggableLabel(Composite parent, MetricCalculator metric) {
		Label label = new Label(parent, SWT.CENTER);
		GridData labelData = new GridData(GridData.CENTER | GridData.FILL_HORIZONTAL);
		label.setLayoutData(labelData);
		DragSource dragSource = new DragSource(label, DND.DROP_MOVE);
		dragSource.setTransfer(getAcceptableTransfers());
		dragSource.addDragListener(new DragMetricListener(label));
		label.setText(metric.name());
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
