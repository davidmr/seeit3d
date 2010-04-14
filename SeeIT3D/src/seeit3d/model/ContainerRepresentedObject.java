package seeit3d.model;

import java.io.Serializable;

public interface ContainerRepresentedObject extends Serializable {

	public String getName();

	public String granularityLevelName(int countLevelsDown);

}
