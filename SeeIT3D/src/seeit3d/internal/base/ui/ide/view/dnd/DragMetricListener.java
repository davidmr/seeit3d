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

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.widgets.Label;

import seeit3d.analysis.metric.MetricCalculator;

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
