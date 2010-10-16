package seeit3d.base.ui.actions;

import seeit3d.base.bus.utils.FunctionToApplyOnContainer;
import seeit3d.base.error.ErrorHandler;
import seeit3d.base.model.Container;
import seeit3d.base.visual.relationships.ISceneGraphRelationshipGenerator;

public final class ApplyChangeRelationShipGenerator extends FunctionToApplyOnContainer {

	private final Class<? extends ISceneGraphRelationshipGenerator> generatorClass;

	public ApplyChangeRelationShipGenerator(Class<? extends ISceneGraphRelationshipGenerator> generatorClass) {
		this.generatorClass = generatorClass;
	}

	@Override
	public Container apply(Container container) {
		try {
			ISceneGraphRelationshipGenerator generator = generatorClass.newInstance();
			container.setSceneGraphRelationshipGenerator(generator);
		} catch (InstantiationException e) {
			ErrorHandler.error(e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			ErrorHandler.error(e);
			e.printStackTrace();
		}
		return container;
	}
}