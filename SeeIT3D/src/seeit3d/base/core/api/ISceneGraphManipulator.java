package seeit3d.base.core.api;

import seeit3d.base.core.handler.SeeIT3DCanvas;
import seeit3d.base.model.Container;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public interface ISceneGraphManipulator {

	void initialize();

	void setupTranslationCallback(PickingCallback callback);

	void unregisterPickingCallback(PickingCallback callback);

	void activateSelectionTool();

	void changeOrbitState(boolean enabled);

	void setViewersPosition(float maxX);

	void removeScene(Container container);

	void clearScene();

	void rebuildSceneGraph();

	SeeIT3DCanvas getCanvas();

}
