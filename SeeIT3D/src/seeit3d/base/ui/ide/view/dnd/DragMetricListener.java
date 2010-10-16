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

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Label;

import seeit3d.base.model.generator.metrics.MetricCalculator;

/**
 * This class listens for metrics that are dragged from the mapping view
 * 
 * @author David Montaño
 * 
 */
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
			MetricCalculator metric = (MetricCalculator) label.getData();
			event.data = metric;
		}
	}

	@Override
	public void dragStart(DragSourceEvent event) {

	}

}
