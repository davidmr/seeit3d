package seeit3d.model;

import seeit3d.model.representation.Container;


public interface IModelCreator {

	public Container analize(boolean includeDependecies);

	public void analizeAndRegisterInView(boolean includeDependecies);

}
