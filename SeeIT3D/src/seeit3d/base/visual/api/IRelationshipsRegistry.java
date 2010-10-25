package seeit3d.base.visual.api;

import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

public interface IRelationshipsRegistry {

	void registerRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generator);

	Iterable<Class<? extends ISceneGraphRelationshipGenerator>> allRelationshipsGenerator();

	String getRelationName(Class<? extends ISceneGraphRelationshipGenerator> selectedGenerator);

	ISceneGraphRelationshipGenerator createNewInstance(Class<? extends ISceneGraphRelationshipGenerator> generator);

}
