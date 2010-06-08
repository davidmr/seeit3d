package seeit3d.core.api;

import seeit3d.core.handler.SeeIT3DCanvas;
import seeit3d.general.SeeIT3DAPI;

public interface SeeIT3DCore extends SeeIT3DAPI {

	SeeIT3DCanvas getMainCanvas();

	boolean isShowRelatedContainers();

}
