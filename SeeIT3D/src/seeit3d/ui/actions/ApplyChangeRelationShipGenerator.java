package seeit3d.ui.actions;

import seeit3d.general.bus.utils.FunctionToApplyOnContainer;
import seeit3d.general.error.ErrorHandler;
import seeit3d.general.model.Container;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

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