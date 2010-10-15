package seeit3d.core.api;

import seeit3d.core.handler.SeeIT3DCanvas;
import seeit3d.general.model.Container;

import com.sun.j3d.utils.picking.behaviors.PickingCallback;

public interface ISceneGraphManipulator {

	void setupTranslationCallback(PickingCallback callback);

	void unregisterPickingCallback(PickingCallback callback);

	void activateSelectionTool();

	void changeOrbitState(boolean enabled);

	SeeIT3DCanvas getCanvas();

	void setViewersPosition(float maxX);

	void removeScene(Container container);

	void clearScene();

	void rebuildSceneGraph();

}
