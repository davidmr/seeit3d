package seeit3d.general.bus.events;

import java.util.Map;

import seeit3d.core.model.Container;
import seeit3d.general.bus.IEvent;

public class SelectedInformationChangedEvent implements IEvent {

	private final Iterable<Container> selectedContainers;

	private final Map<String, String> currentMetricsValuesFromSelection;

	public SelectedInformationChangedEvent(Iterable<Container> selectedContainers, Map<String, String> currentMetricsValuesFromSelection) {
		this.selectedContainers = selectedContainers;
		this.currentMetricsValuesFromSelection = currentMetricsValuesFromSelection;
	}

	public Iterable<Container> getSelectedContainers() {
		return selectedContainers;
	}

	public Map<String, String> getCurrentMetricsValuesFromSelection() {
		return currentMetricsValuesFromSelection;
	}

}
