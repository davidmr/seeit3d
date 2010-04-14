package seeit3d.manager;

import java.util.Iterator;
import java.util.Map;

import seeit3d.model.representation.Container;

public interface SelectionInformationAware {
	
	void updateInformation(Iterator<Container> selectedContainers, Map<String, String> metricValues);
	
}
