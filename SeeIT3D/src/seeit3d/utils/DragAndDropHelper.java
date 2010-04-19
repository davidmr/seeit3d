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
package seeit3d.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import seeit3d.metrics.BaseMetricCalculator;
import seeit3d.view.dnd.DragMetricListener;
import seeit3d.view.dnd.TransferMetric;

/**
 * This class provide utility methods to create register and handle different options of Drag and Drop operations for mapping view
 * 
 * @author David Montaño
 * 
 */
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
