package seeit3d.base.ui.actions;

import seeit3d.base.SeeIT3D;
import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.model.Container;
import seeit3d.base.visual.api.IRelationshipsRegistry;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

import com.google.inject.Inject;

public final class ApplyChangeRelationShipGenerator extends FunctionToApplyOnContainer {

	private final Class<? extends ISceneGraphRelationshipGenerator> generatorClass;

	private IRelationshipsRegistry relationshipsRegistry;

	public ApplyChangeRelationShipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generatorClass) {
		SeeIT3D.injector().injectMembers(this);
		this.generatorClass = generatorClass;
	}

	@Override
	public Container apply(Container container) {
		ISceneGraphRelationshipGenerator generator = relationshipsRegistry.createNewInstance(generatorClass);
		container.setSceneGraphRelationshipGenerator(generator);
		return container;
	}

	@Inject
	public void setRelationshipsRegistry(IRelationshipsRegistry relationshipsRegistry) {
		this.relationshipsRegistry = relationshipsRegistry;
	}
}