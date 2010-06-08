package seeit3d.visual.relationships.imp;

import java.util.ArrayList;
import java.util.List;

import seeit3d.general.model.Container;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

public class NoRelationships implements ISceneGraphRelationshipGenerator {

	private static final String NAME = "No Relationships";

	@Override
	public List<Container> generateVisualRelationShips(Container baseContainer) {
		return new ArrayList<Container>();
	}

	@Override
	public String getName() {
		return NAME;
	}

}
