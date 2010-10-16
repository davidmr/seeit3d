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
package seeit3d.base.ui.ide.view.listeners;

import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import seeit3d.base.bus.IEvent;
import seeit3d.base.bus.IEventListener;
import seeit3d.base.bus.events.SelectedInformationChangedEvent;
import seeit3d.base.model.Container;

/**
 * This class is the feedback listener that is shown to the user
 * 
 * @author David Montaño
 * 
 */

public class LabelInformation implements IEventListener {

	private final Label label;

	public LabelInformation(Label label) {
		this.label = label;
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof SelectedInformationChangedEvent) {
			SelectedInformationChangedEvent information = (SelectedInformationChangedEvent) event;
			Iterable<Container> selectedContainers = information.getSelectedContainers();
			Map<String, String> metricValues = information.getCurrentMetricsValuesFromSelection();

			String formattedString = "";
			if (!selectedContainers.iterator().hasNext()) {
				formattedString += "Select a Container to show information";
			} else {
				formattedString += "Current selected container: ";
			}
			for (Container container : selectedContainers) {
				formattedString += container.getName();
				formattedString += ",";
			}
			formattedString = formattedString.replaceAll(",$", "");
			formattedString += "\n";
			if (metricValues.isEmpty()) {
				formattedString += "Select a Polycylinder to show information";
			} else {
				formattedString += "Metric Values: ";
				for (Map.Entry<String, String> entry : metricValues.entrySet()) {
					formattedString += entry.getKey() + ": " + entry.getValue() + " | ";
				}
			}
			final String stringToShow = formattedString;
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					label.setText(stringToShow);
				}
			});
		}

	}

}
