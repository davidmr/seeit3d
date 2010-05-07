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
package seeit3d.view.listeners;

import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import seeit3d.manager.SelectionInformationAware;
import seeit3d.model.representation.Container;

/**
 * This class is the feedback listener that is shown to the user
 * 
 * @author David Montaño
 * 
 */
public class LabelInformation implements SelectionInformationAware {

	private final Label label;

	public LabelInformation(Label label) {
		this.label = label;
	}

	@Override
	public void updateInformation(Iterable<Container> selectedContainers, Map<String, String> metricValues) {
		final StringBuilder formattedString = new StringBuilder();
		if (!selectedContainers.iterator().hasNext()) {
			formattedString.append("Select a Container to show information");
		}else{
			formattedString.append("Current selected container: ");
		}
		for (Container container : selectedContainers) {
			formattedString.append(container.getName());
			formattedString.append(",");
		}
		formattedString.deleteCharAt(formattedString.length() - 2).append("\n");
		if (metricValues.isEmpty()) {
			formattedString.append("Select a Polycylinder to show information");
		} else {
			formattedString.append("Metric Values: ");
			for (Map.Entry<String, String> entry : metricValues.entrySet()) {
				formattedString.append(entry.getKey()).append(": ").append(entry.getValue()).append(" | ");
			}
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				label.setText(formattedString.toString());
			}
		});

	}
}
