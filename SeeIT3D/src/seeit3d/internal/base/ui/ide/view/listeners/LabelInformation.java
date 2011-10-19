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

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import seeit3d.internal.base.bus.IEvent;
import seeit3d.internal.base.bus.IEventListener;
import seeit3d.internal.base.bus.events.SelectedInformationChangedEvent;

/**
 * This class is the feedback listener that is shown to the user
 * 
 * @author David Montaño
 * 
 */

public class LabelInformation implements IEventListener {

	private final StyledText text;

	public LabelInformation(StyledText text) {
		this.text = text;
	}

	@Override
	public void processEvent(IEvent event) {
		if (event instanceof SelectedInformationChangedEvent) {
			final SelectedInformationChangedEvent information = (SelectedInformationChangedEvent) event;
			
			Map<String, String> metricValues = information.getCurrentMetricsValuesFromSelection();
			String formattedString = "";
			formattedString += information.isContainerSelected() ? "Current selected container: " : "Select a Container to show information";
			formattedString += information.getSelectedContainersName();
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
					text.setText(stringToShow);
					Collection<String> metrics = information.metricNamesInMapping();
					for (String metric : metrics) {
						StyleRange style = new StyleRange();
						style.fontStyle = SWT.BOLD;
						style.start = stringToShow.indexOf(metric + ":");
						style.length = metric.length();
						text.setStyleRange(style);
					}
					
				}
			});
		}

	}
}
