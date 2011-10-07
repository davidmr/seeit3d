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
package seeit3d.internal.base.ui.ide.view.listeners;

import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.SelectedInformationChangedEvent;
import seeit3d.internal.base.model.Container;

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
				formattedString += "Metric Values for " + information.getSelectedPolycylindersName() + ": ";
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
