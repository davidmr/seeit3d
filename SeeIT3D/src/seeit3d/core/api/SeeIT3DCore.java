package seeit3d.core.api;

import java.io.*;
import java.util.List;

import javax.vecmath.Color3f;

import seeit3d.core.handler.SeeIT3DCanvas;
import seeit3d.core.model.*;
import seeit3d.core.model.generator.metrics.MetricCalculator;
import seeit3d.visual.colorscale.IColorScale;
import seeit3d.visual.relationships.ISceneGraphRelationshipGenerator;

public interface SeeIT3DCore {

	@Deprecated
	SeeIT3DCanvas getMainCanvas();

	void addContainerToView(Container container);

	void clearContainers();

	void updateSelectedContainersMapping(MetricCalculator metric, VisualProperty visualProp);

	void removeSelectContainersMapping(MetricCalculator metric);

	void updateCurrentSelectedContainer();

	void deleteSelectedContainers();

	void deleteAllContainers();

	void changeSelectionAndUpdateMappingView(Container newContainer, PolyCylinder polycylinder, boolean toggleContainerSelection, boolean togglePolycylinderSelection);

	void changeContainerSelection(boolean increase);

	void scaleCurrentContainer(boolean scaleUp);

	void updateViewUsingLevelOnSelectedContainer(boolean nextLevel);

	void resetVisualization();

	@Deprecated
	void changeTransparencyPolyCylindersSelection(boolean moreTransparent);

	@Deprecated
	void sortPolyCylinders();

	void loadVisualization(InputStream input) throws IOException;

	void saveVisualization(OutputStream output) throws IOException;

	@Deprecated
	void changeCurrentSortingPolyCylindersProperty(VisualProperty visualProperty);

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
	IColorScale getColorScale();

	@Deprecated
	int getPolycylindersPerRow();

	@Deprecated
	Color3f getHighlightColor();

	@Deprecated
	Color3f getRelationMarkColor();

	@Deprecated
	float getTransparencyStep();

	@Deprecated
	void setColorScale(IColorScale colorScale);

	@Deprecated
	void useSceneGraphRelationshipGenerator(Class<? extends ISceneGraphRelationshipGenerator> sceneGraphRelationshipGenerator);

	@Deprecated
	void setShowRelatedContainers(boolean showRelated);

	@Deprecated
	boolean isShowRelatedContainers();

}
