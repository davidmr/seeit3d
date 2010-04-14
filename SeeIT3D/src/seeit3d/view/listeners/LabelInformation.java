package seeit3d.view.listeners;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import seeit3d.manager.SelectionInformationAware;
import seeit3d.model.representation.Container;

public class LabelInformation implements SelectionInformationAware {

	private final Label label;

	public LabelInformation(Label label) {
		this.label = label;
	}

	@Override
	public void updateInformation(Iterator<Container> selectedContainers, Map<String, String> metricValues) {
		final StringBuilder formattedString = new StringBuilder();
		if (!selectedContainers.hasNext()) {
			formattedString.append("Select a Container to show information");
		}else{
			formattedString.append("Current selected container: ");
		}
		while (selectedContainers.hasNext()) {
			Container container = selectedContainers.next();
			formattedString.append(container.getName());
			if (selectedContainers.hasNext()) {
				formattedString.append(",");
			}
		}
		formattedString.append("\n");
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
