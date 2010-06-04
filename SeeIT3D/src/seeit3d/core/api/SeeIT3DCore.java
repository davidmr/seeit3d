package seeit3d.core.api;

import java.util.List;

import seeit3d.core.handler.SeeIT3DCanvas;
import seeit3d.core.model.Container;
import seeit3d.core.model.VisualProperty;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

public interface SeeIT3DCore {

	@Deprecated
	SeeIT3DCanvas getMainCanvas();

	/**
	 * Should be handled internally
	 */
	@Deprecated
	void refreshVisualization();

	Iterable<Container> containersInView();

	@Deprecated
	List<Container> getCurrentSelectedContainers();

	@Deprecated
	VisualProperty getCurrentSortingProperty();

	String getCurrentSelectedContainersAsString();

	@Deprecated
	void useSceneGraphRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> sceneGraphRelationshipGenerator);

	@Deprecated
	void setShowRelatedContainers(boolean showRelated);

	@Deprecated
	boolean isShowRelatedContainers();

}
