package seeit3d.model;

import seeit3d.model.representation.Container;
import seeit3d.utils.Utils;

public class NoOpCreator implements IModelCreator {

	@Override
	public Container analize(boolean includeDependecies) {
		return Utils.emptyContainer();
	}

	@Override
	public void analizeAndRegisterInView(boolean includeDependecies) {

	}

}
