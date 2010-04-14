package seeit3d.model.xml;

import seeit3d.model.ContainerRepresentedObject;
import seeit3d.model.xml.internal.Container;

public class XMLBasedBasicRepresentation implements ContainerRepresentedObject {

	private static final long serialVersionUID = -5819100372296201970L;

	private final Container container;

	public XMLBasedBasicRepresentation(Container container) {
		this.container = container;
	}

	@Override
	public String getName() {
		return container.getName();
	}

	@Override
	public String granularityLevelName(int countLevelsDown) {
		return container.getGranularityLevelName();
	}

}
