package seeit3d.relationships.imp;

import java.util.ArrayList;
import java.util.List;

import seeit3d.model.representation.Container;
import seeit3d.relationships.RelationShipVisualGenerator;

public class NoRelationships implements RelationShipVisualGenerator {

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
