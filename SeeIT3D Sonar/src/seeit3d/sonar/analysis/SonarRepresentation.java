package seeit3d.sonar.analysis;

import seeit3d.analysis.IContainerRepresentedObject;

public class SonarRepresentation implements IContainerRepresentedObject {

	private static final long serialVersionUID = -2327973469903225226L;

	private final String artifactId;

	public SonarRepresentation(String artifactId) {
		this.artifactId = artifactId;
	}

	@Override
	public String getName() {
		return artifactId;
	}

	@Override
	public String granularityLevelName(int countLevelsDown) {
		return "Level " + countLevelsDown;
	}

}
